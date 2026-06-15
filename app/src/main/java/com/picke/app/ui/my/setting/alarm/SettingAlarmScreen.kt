package com.picke.app.ui.my.setting.alarm

import android.Manifest
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.messaging.FirebaseMessaging
import com.picke.app.R
import com.picke.app.domain.model.NotificationSettingsBoard
import com.picke.app.ui.component.CustomTopAppBar
import com.picke.app.ui.component.NotificationPermissionBottomSheet
import com.picke.app.ui.theme.SwypAppTheme
import com.picke.app.ui.theme.SwypTheme

@Composable
fun SettingAlarmScreen(
    onBackClick: () -> Unit,
    viewModel: SettingAlarmViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SettingAlarmContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onUpdateSetting = viewModel::updateSetting
    )
}

@Composable
private fun SettingAlarmContent(
    uiState: SettingAlarmUiState,
    onBackClick: () -> Unit,
    onUpdateSetting: (NotificationSettingsBoard) -> Unit
) {
    val context = LocalContext.current
    val settings = uiState.settings

    var showPermissionSheet by remember { mutableStateOf(false) }
    val pendingToggleAction = remember { mutableStateOf<(() -> Unit)?>(null) }

    val scrollState = rememberScrollState()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            pendingToggleAction.value?.invoke()
            pendingToggleAction.value = null
            fetchFcmToken()
        }
    }

    val onToggleTurnedOn: (() -> Unit) -> Unit = { applyChange ->
        val notificationsEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled()
        if (notificationsEnabled) {
            applyChange()
        } else {
            pendingToggleAction.value = applyChange
            showPermissionSheet = true
        }
    }

    Scaffold(
        containerColor = SwypTheme.colors.backgroundBrand,
        modifier = Modifier.systemBarsPadding(),
        topBar = {
            CustomTopAppBar(
                title = stringResource(R.string.my_setting_alarm),
                centerTitle = true,
                showLogo = false,
                showBackButton = true,
                onBackClick = { onBackClick() },
                backgroundColor = SwypTheme.colors.backgroundBrand
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading && settings == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = SwypTheme.colors.primary)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(scrollState)
            ) {
                // 1. 기능별 알림 설정
                AlarmCategoryHeader(title = stringResource(id = R.string.setting_alarm_category_function))
                AlarmSettingItem(
                    title = stringResource(id = R.string.setting_alarm_new_battle_title),
                    subtitle = stringResource(id = R.string.setting_alarm_new_battle_desc),
                    isChecked = settings?.newBattleEnabled ?: false,
                    onCheckedChange = { checked ->
                        if (checked) onToggleTurnedOn {
                            settings?.let { onUpdateSetting(it.copy(newBattleEnabled = true)) }
                        } else settings?.let { onUpdateSetting(it.copy(newBattleEnabled = false)) }
                    }
                )
                AlarmDivider()
                AlarmSettingItem(
                    title = stringResource(id = R.string.setting_alarm_vote_result_title),
                    subtitle = stringResource(id = R.string.setting_alarm_vote_result_desc),
                    isChecked = settings?.battleResultEnabled ?: false,
                    onCheckedChange = { checked ->
                        if (checked) onToggleTurnedOn {
                            settings?.let { onUpdateSetting(it.copy(battleResultEnabled = true)) }
                        } else settings?.let { onUpdateSetting(it.copy(battleResultEnabled = false)) }
                    }
                )
                AlarmDivider()

                // 2. 소셜 알림 설정
                AlarmCategoryHeader(title = stringResource(id = R.string.setting_alarm_category_social))
                AlarmSettingItem(
                    title = stringResource(id = R.string.setting_alarm_reply_title),
                    subtitle = stringResource(id = R.string.setting_alarm_reply_desc),
                    isChecked = settings?.commentReplyEnabled ?: false,
                    onCheckedChange = { checked ->
                        if (checked) onToggleTurnedOn {
                            settings?.let { onUpdateSetting(it.copy(commentReplyEnabled = true)) }
                        } else settings?.let { onUpdateSetting(it.copy(commentReplyEnabled = false)) }
                    }
                )
                AlarmDivider()
                AlarmSettingItem(
                    title = stringResource(id = R.string.setting_alarm_new_comment_title),
                    subtitle = stringResource(id = R.string.setting_alarm_new_comment_desc),
                    isChecked = settings?.newCommentEnabled ?: false,
                    onCheckedChange = { checked ->
                        if (checked) onToggleTurnedOn {
                            settings?.let { onUpdateSetting(it.copy(newCommentEnabled = true)) }
                        } else settings?.let { onUpdateSetting(it.copy(newCommentEnabled = false)) }
                    }
                )
                AlarmDivider()
                AlarmSettingItem(
                    title = stringResource(id = R.string.setting_alarm_like_title),
                    subtitle = stringResource(id = R.string.setting_alarm_like_desc),
                    isChecked = settings?.contentLikeEnabled ?: false,
                    onCheckedChange = { checked ->
                        if (checked) onToggleTurnedOn {
                            settings?.let { onUpdateSetting(it.copy(contentLikeEnabled = true)) }
                        } else settings?.let { onUpdateSetting(it.copy(contentLikeEnabled = false)) }
                    }
                )
                AlarmDivider()

                // 3. 마케팅 알림 설정
                AlarmCategoryHeader(title = stringResource(id = R.string.setting_alarm_category_marketing))
                AlarmSettingItem(
                    title = stringResource(id = R.string.setting_alarm_marketing_title),
                    subtitle = stringResource(id = R.string.setting_alarm_marketing_desc),
                    isChecked = settings?.marketingEventEnabled ?: false,
                    onCheckedChange = { checked ->
                        if (checked) onToggleTurnedOn {
                            settings?.let { onUpdateSetting(it.copy(marketingEventEnabled = true)) }
                        } else settings?.let { onUpdateSetting(it.copy(marketingEventEnabled = false)) }
                    }
                )
                AlarmDivider()

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }

    if (showPermissionSheet) {
        NotificationPermissionBottomSheet(
            onDismiss = {
                showPermissionSheet = false
                pendingToggleAction.value = null
            },
            onAgree = {
                showPermissionSheet = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                    }
                    context.startActivity(intent)
                    pendingToggleAction.value = null
                }
            },
            onDisagree = {
                showPermissionSheet = false
                pendingToggleAction.value = null
            }
        )
    }
}

private fun fetchFcmToken() {
    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val token = task.result
            Log.d("FCM", "토큰 발급 완료: $token")
            // TODO: 서버 FCM 토큰 등록 API 연동
        } else {
            Log.w("FCM", "토큰 발급 실패", task.exception)
        }
    }
}

@Composable
fun AlarmCategoryHeader(title: String) {
    Text(
        text = title,
        style = SwypTheme.typography.b5Medium,
        color = SwypTheme.colors.textSecondary,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp)
    )
}

@Composable
fun AlarmSettingItem(
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = SwypTheme.typography.b4Medium,
                color = SwypTheme.colors.textPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = SwypTheme.typography.caption2Medium,
                color = SwypTheme.colors.neutral400
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Switch(
            modifier = Modifier.scale(0.8f),
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = SwypTheme.colors.primary,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = SwypTheme.colors.textMuted,
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
}

@Composable
fun AlarmDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp),
        thickness = 1.dp,
        color = SwypTheme.colors.borderDefault
    )
}

@Preview(showBackground = true, showSystemUi = true, name = "알림 설정 화면")
@Composable
private fun SettingAlarmScreenPreview() {
    SwypAppTheme {
        SettingAlarmContent(
            uiState = SettingAlarmUiState(
                settings = NotificationSettingsBoard(
                    newBattleEnabled = true,
                    battleResultEnabled = true,
                    commentReplyEnabled = true,
                    newCommentEnabled = false,
                    contentLikeEnabled = false,
                    marketingEventEnabled = true
                )
            ),
            onBackClick = {},
            onUpdateSetting = {}
        )
    }
}

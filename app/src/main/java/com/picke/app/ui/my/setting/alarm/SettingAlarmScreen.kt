package com.picke.app.ui.my.setting.alarm

import android.Manifest
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
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
import com.google.firebase.messaging.FirebaseMessaging
import com.picke.app.R
import com.picke.app.ui.component.CustomTopAppBar
import com.picke.app.ui.notification.NotificationPermissionBottomSheet
import com.picke.app.ui.theme.Beige200
import com.picke.app.ui.theme.Beige600
import com.picke.app.ui.theme.Gray300
import com.picke.app.ui.theme.Gray400
import com.picke.app.ui.theme.Gray700
import com.picke.app.ui.theme.Gray900
import com.picke.app.ui.theme.SwypAppTheme
import com.picke.app.ui.theme.SwypTheme
import com.picke.app.ui.theme.White

@Composable
fun SettingAlarmScreen(
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current

    var isNewBattleEnabled by remember { mutableStateOf(false) }
    var isVoteResultEnabled by remember { mutableStateOf(true) }
    var isReplyEnabled by remember { mutableStateOf(true) }
    var isNewCommentEnabled by remember { mutableStateOf(false) }
    var isLikeEnabled by remember { mutableStateOf(false) }
    var isMarketingEnabled by remember { mutableStateOf(true) }

    var showPermissionSheet by remember { mutableStateOf(false) }
    // 권한 획득 후 실행할 토글 변경 액션을 임시 보관
    val pendingToggleAction = remember { mutableStateOf<(() -> Unit)?>(null) }

    val scrollState = rememberScrollState()

    // Android 13+ 알림 권한 요청 런처
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            pendingToggleAction.value?.invoke()
            pendingToggleAction.value = null
            fetchFcmToken()
        }
    }

    // 토글 ON 시 호출: 알림 권한 확인 후 분기
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
        containerColor = Beige200,
        modifier = Modifier.systemBarsPadding(),
        topBar = {
            CustomTopAppBar(
                title = stringResource(R.string.my_setting_alarm),
                centerTitle = true,
                showLogo = false,
                showBackButton = true,
                onBackClick = { onBackClick() },
                backgroundColor = Beige200
            )
        }
    ) { innerPadding ->
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
                isChecked = isNewBattleEnabled,
                onCheckedChange = { checked ->
                    if (checked) onToggleTurnedOn { isNewBattleEnabled = true }
                    else isNewBattleEnabled = false
                }
            )
            AlarmDivider()
            AlarmSettingItem(
                title = stringResource(id = R.string.setting_alarm_vote_result_title),
                subtitle = stringResource(id = R.string.setting_alarm_vote_result_desc),
                isChecked = isVoteResultEnabled,
                onCheckedChange = { checked ->
                    if (checked) onToggleTurnedOn { isVoteResultEnabled = true }
                    else isVoteResultEnabled = false
                }
            )
            AlarmDivider()

            // 2. 소셜 알림 설정
            AlarmCategoryHeader(title = stringResource(id = R.string.setting_alarm_category_social))
            AlarmSettingItem(
                title = stringResource(id = R.string.setting_alarm_reply_title),
                subtitle = stringResource(id = R.string.setting_alarm_reply_desc),
                isChecked = isReplyEnabled,
                onCheckedChange = { checked ->
                    if (checked) onToggleTurnedOn { isReplyEnabled = true }
                    else isReplyEnabled = false
                }
            )
            AlarmDivider()
            AlarmSettingItem(
                title = stringResource(id = R.string.setting_alarm_new_comment_title),
                subtitle = stringResource(id = R.string.setting_alarm_new_comment_desc),
                isChecked = isNewCommentEnabled,
                onCheckedChange = { checked ->
                    if (checked) onToggleTurnedOn { isNewCommentEnabled = true }
                    else isNewCommentEnabled = false
                }
            )
            AlarmDivider()
            AlarmSettingItem(
                title = stringResource(id = R.string.setting_alarm_like_title),
                subtitle = stringResource(id = R.string.setting_alarm_like_desc),
                isChecked = isLikeEnabled,
                onCheckedChange = { checked ->
                    if (checked) onToggleTurnedOn { isLikeEnabled = true }
                    else isLikeEnabled = false
                }
            )
            AlarmDivider()

            // 3. 마케팅 알림 설정
            AlarmCategoryHeader(title = stringResource(id = R.string.setting_alarm_category_marketing))
            AlarmSettingItem(
                title = stringResource(id = R.string.setting_alarm_marketing_title),
                subtitle = stringResource(id = R.string.setting_alarm_marketing_desc),
                isChecked = isMarketingEnabled,
                onCheckedChange = { checked ->
                    if (checked) onToggleTurnedOn { isMarketingEnabled = true }
                    else isMarketingEnabled = false
                }
            )
            AlarmDivider()

            Spacer(modifier = Modifier.height(40.dp))
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
                    // Android 13+: 런타임 권한 요청
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    // Android 12 이하: 시스템 알림 설정으로 이동
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
        color = Gray700,
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
                color = Gray900
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = SwypTheme.typography.caption2Medium,
                color = Gray400
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Switch(
            modifier = Modifier.scale(0.8f),
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = White,
                checkedTrackColor = SwypTheme.colors.primary,
                uncheckedThumbColor = White,
                uncheckedTrackColor = Gray300,
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
        color = Beige600
    )
}

@Preview(showBackground = true, showSystemUi = true, name = "알림 설정 화면")
@Composable
private fun SettingAlarmScreenPreview() {
    SwypAppTheme {
        SettingAlarmScreen(
            onBackClick = {}
        )
    }
}
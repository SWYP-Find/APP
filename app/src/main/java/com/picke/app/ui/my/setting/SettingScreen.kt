package com.picke.app.ui.my.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.picke.app.R
import com.picke.app.ui.component.CustomConfirmDialog
import com.picke.app.ui.component.CustomTopAppBar
import com.picke.app.ui.theme.Beige200
import com.picke.app.ui.theme.Beige600
import com.picke.app.ui.theme.Gray700
import com.picke.app.ui.theme.Gray900
import com.picke.app.ui.theme.Primary900
import com.picke.app.ui.theme.SwypTheme

@Composable
fun SettingScreen(
    onBackClick: ()->Unit,
    onNavigateToSettingProfile: ()->Unit,
    onNavigateToSettingAlarm: ()->Unit,
    onNavigateToPrivacyPolicy: () -> Unit,
    onNavigateToTermsOfService: () -> Unit,
    onNavigateToWithdraw: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: SettingViewModel = hiltViewModel()
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showWithdrawDialog by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar={
                CustomTopAppBar(
                    title = stringResource(R.string.setting),
                    centerTitle = true,
                    showLogo = false,
                    showBackButton = true,
                    onBackClick = { onBackClick() },
                    backgroundColor = Beige200
                )
            },
            containerColor = Beige200
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(top = innerPadding.calculateTopPadding())
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                /*SettingMenuItem(
                    title = stringResource(R.string.my_setting_alarm),
                    onClick = {
                        onNavigateToSettingAlarm()
                    }
                )*/
                SettingMenuItem(
                    title = stringResource(R.string.setting_footer_privacy),
                    onClick = onNavigateToPrivacyPolicy
                )
                SettingMenuItem(
                    title = stringResource(R.string.setting_footer_terms),
                    onClick = onNavigateToTermsOfService
                )
                SettingMenuItem(
                    title = stringResource(R.string.logout),
                    onClick = {
                        showLogoutDialog = true
                    }
                )
                SettingMenuItem(
                    title = stringResource(R.string.withdraw),
                    onClick = {
                        showWithdrawDialog = true
                    }
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            if (showLogoutDialog) {
                CustomConfirmDialog(
                    message = "로그아웃 시 원활한 이용이 어려울 수 있습니다.\n그럼에도 로그아웃하시겠습니까?",
                    confirmText = "네, 로그아웃합니다",
                    dismissText = "뒤로가기",
                    onConfirm = {
                        showLogoutDialog = false
                        viewModel.logout()
                        onNavigateToLogin()
                    },
                    onDismiss = {
                        showLogoutDialog = false
                    }
                )
            }

            if (showWithdrawDialog) {
                CustomConfirmDialog(
                    message = "탈퇴 시 지금까지의 이용기록이 영구 삭제 됩니다.\n그럼에도 탈퇴하시겠습니까?",
                    confirmText = "네, 탈퇴합니다",
                    dismissText = "뒤로가기",
                    onConfirm = {
                        showWithdrawDialog = false
                        onNavigateToWithdraw()
                    },
                    onDismiss = {
                        showWithdrawDialog = false
                    }
                )
            }
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(enabled = false) { /* 클릭 방지 */ },
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Primary900)
            }
        }
    }
}


@Composable
fun SettingMenuItem(
    title: String,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier.clickable{ onClick() }
            .fillMaxWidth(),
    ){
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = title,
                style = SwypTheme.typography.b3SemiBold,
                color = Gray700
            )
            Icon(
                painterResource(R.drawable.ic_arrow_right_a),
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                tint = Gray900
            )
        }
        HorizontalDivider(color = Beige600, thickness = 1.dp)
    }
}
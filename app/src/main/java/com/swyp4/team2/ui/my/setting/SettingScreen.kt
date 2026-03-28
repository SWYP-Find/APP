package com.swyp4.team2.ui.my.setting

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.swyp4.team2.R
import com.swyp4.team2.ui.component.CustomTopAppBar
import com.swyp4.team2.ui.theme.Beige200
import com.swyp4.team2.ui.theme.Beige600
import com.swyp4.team2.ui.theme.Gray200
import com.swyp4.team2.ui.theme.Gray300
import com.swyp4.team2.ui.theme.Gray700
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.SwypTheme

@Composable
fun SettingScreen(
    onBackClick: ()->Unit,
    onNavigateToSettingProfile: ()->Unit,
    onNavigateToSettingAlarm: ()->Unit,
    onNavigateToPrivacyPolicy: () -> Unit,
    onNavigateToTermsOfService: () -> Unit
) {
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
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ){
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
                    // 로그아웃 하기
                }
            )
            SettingMenuItem(
                title = stringResource(R.string.withdraw),
                onClick = {
                    // 탈퇴 하기
                }
            )
            Spacer(modifier = Modifier.weight(1f))
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
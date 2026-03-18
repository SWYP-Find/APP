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
import com.swyp4.team2.ui.theme.Gray200
import com.swyp4.team2.ui.theme.Gray300
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.SwypTheme

@Composable
fun SettingScreen(
    onBackClick: ()->Unit,
    onNavigateToSettingProfile: ()->Unit,
    onNavigateToSettingAlarm: ()->Unit,
    onOpenWebLink: (String)->Unit,
) {
    Scaffold(
        topBar={
            CustomTopAppBar(
                title = stringResource(R.string.setting),
                centerTitle = true,
                showLogo = false,
                showBackButton = true,
                onBackClick = { onBackClick() },
                backgroundColor = SwypTheme.colors.background
            )
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ){
            SettingMenuItem(
                iconResId =  R.drawable.ic_user_profile,
                title = stringResource(R.string.my_setting_profile),
                onClick = {
                    onNavigateToSettingProfile()
                }
            )
            SettingMenuItem(
                iconResId =  R.drawable.ic_alarm_setting,
                title = stringResource(R.string.my_setting_alarm),
                onClick = {
                    onNavigateToSettingAlarm()
                }
            )
            SettingMenuItem(
                iconResId =  R.drawable.ic_help,
                title = stringResource(R.string.help_and_opinion),
                onClick = {
                    // 웹 링크 열기
                }
            )
            SettingMenuItem(
                iconResId =  R.drawable.ic_logout,
                title = stringResource(R.string.logout),
                onClick = {
                    // 로그아웃 하기
                }
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FooterLinkText(R.string.setting_footer_privacy) {
                    onOpenWebLink("https://privacy-policy.com")
                }
                Text(
                    text = " ${stringResource(R.string.setting_footer_divider)} ",
                    style = SwypTheme.typography.b4Regular,
                    color = Gray300
                )
                FooterLinkText(R.string.setting_footer_terms) {
                    onOpenWebLink("https://terms-of-service.com")
                }
            }
        }
    }
}

@Composable
fun FooterLinkText(
    @StringRes textResId: Int,
    onClick: () -> Unit
) {
    Text(
        text = stringResource(id = textResId),
        style = SwypTheme.typography.b4Regular,
        color = Gray300,
        modifier = Modifier.clickable { onClick() }
    )
}

@Composable
fun SettingMenuItem(
    iconResId: Int,
    title: String,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier.clickable{ onClick() }
            .fillMaxWidth(),
    ){
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                // modifier = Modifier.weight(1f, fill=false)
            ){
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = Gray900
                )
                Spacer(modifier = Modifier.width(12.dp)) // 아이콘과 글자 사이 고정 간격
                Text(
                    text = title,
                    style = SwypTheme.typography.b1Medium,
                    color = Gray900
                )
            }
            Icon(
                painterResource(R.drawable.ic_arrow_right),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = Gray900
            )
        }
        HorizontalDivider(color = Gray200, thickness = 1.dp)
    }
}
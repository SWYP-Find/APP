package com.swyp4.team2.ui.my.setting.alarm

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.swyp4.team2.R
import com.swyp4.team2.ui.component.CustomTopAppBar
import com.swyp4.team2.ui.theme.Beige400
import com.swyp4.team2.ui.theme.Gray300
import com.swyp4.team2.ui.theme.Gray500
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.SwypTheme

@Composable
fun SettingAlarmScreen(
    onBackClick: () -> Unit,
) {
    var isNewBattleEnabled by remember { mutableStateOf(false) }
    var isVoteResultEnabled by remember { mutableStateOf(true) }

    var isReplyEnabled by remember { mutableStateOf(true) }
    var isNewCommentEnabled by remember { mutableStateOf(false) }
    var isLikeEnabled by remember { mutableStateOf(false) }

    var isMarketingEnabled by remember { mutableStateOf(true) }

    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        topBar={
            CustomTopAppBar(
                title = stringResource(R.string.my_setting_alarm),
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
                onCheckedChange = { isNewBattleEnabled = it }
            )
            AlarmDivider()
            AlarmSettingItem(
                title = stringResource(id = R.string.setting_alarm_vote_result_title),
                subtitle = stringResource(id = R.string.setting_alarm_vote_result_desc),
                isChecked = isVoteResultEnabled,
                onCheckedChange = { isVoteResultEnabled = it }
            )
            AlarmDivider()

            // 2. 소셜 알림 설정
            AlarmCategoryHeader(title = stringResource(id = R.string.setting_alarm_category_social))
            AlarmSettingItem(
                title = stringResource(id = R.string.setting_alarm_reply_title),
                subtitle = stringResource(id = R.string.setting_alarm_reply_desc),
                isChecked = isReplyEnabled,
                onCheckedChange = { isReplyEnabled = it }
            )
            AlarmDivider()
            AlarmSettingItem(
                title = stringResource(id = R.string.setting_alarm_new_comment_title),
                subtitle = stringResource(id = R.string.setting_alarm_new_comment_desc),
                isChecked = isNewCommentEnabled,
                onCheckedChange = { isNewCommentEnabled = it }
            )
            AlarmDivider()
            AlarmSettingItem(
                title = stringResource(id = R.string.setting_alarm_like_title),
                subtitle = stringResource(id = R.string.setting_alarm_like_desc),
                isChecked = isLikeEnabled,
                onCheckedChange = { isLikeEnabled = it }
            )
            AlarmDivider()

            // 3. 마케팅 알림 설정
            AlarmCategoryHeader(title = stringResource(id = R.string.setting_alarm_category_marketing))
            AlarmSettingItem(
                title = stringResource(id = R.string.setting_alarm_marketing_title),
                subtitle = stringResource(id = R.string.setting_alarm_marketing_desc),
                isChecked = isMarketingEnabled,
                onCheckedChange = { isMarketingEnabled = it }
            )
            AlarmDivider()

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun AlarmCategoryHeader(title: String) {
    Text(
        text = title,
        style = SwypTheme.typography.labelMedium,
        color = Gray500,
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
                style = SwypTheme.typography.b2Medium,
                color = Gray900
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = SwypTheme.typography.labelXSmall,
                color = Gray500
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // 커스텀 스위치
        Switch(
            modifier = Modifier.scale(0.8f),
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = SwypTheme.colors.primary,
                uncheckedThumbColor = Color.White,
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
        color = Beige400
    )
}
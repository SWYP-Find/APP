package com.swyp4.team2.ui.home.todaypick

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.swyp4.team2.ui.theme.SwypTheme

@Composable
fun TodayPickeScreen(
    onBackClick: ()->Unit,
) {
    Text(
        text = "오늘의 픽 배틀이지롱",
        style = SwypTheme.typography.h1SemiBold,
    )
}
package com.picke.app.ui.home.todaypick

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.picke.app.ui.theme.SwypTheme

@Composable
fun TodayPickeScreen(
    onBackClick: ()->Unit,
) {
    Text(
        text = "오늘의 픽 배틀이지롱",
        style = SwypTheme.typography.h1SemiBold,
    )
}
package com.picke.presentation.ui.home.todaypick

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.picke.presentation.ui.theme.PickeTheme

@Composable
fun TodayPickeScreen(
    onBackClick: ()->Unit,
) {
    Text(
        text = "오늘의 픽 배틀이지롱",
        style = PickeTheme.typography.h1SemiBold,
    )
}
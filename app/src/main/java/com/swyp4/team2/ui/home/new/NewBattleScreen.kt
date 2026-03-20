package com.swyp4.team2.ui.home.new

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.swyp4.team2.ui.theme.SwypTheme

@Composable
fun NewBattleScreen(
    onBackClick: ()->Unit,
) {
    Text(
        text = "뉴 배틀이지롱",
        style = SwypTheme.typography.h1SemiBold,
    )
}
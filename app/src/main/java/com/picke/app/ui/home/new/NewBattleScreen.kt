package com.picke.app.ui.home.new

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.picke.app.ui.theme.SwypTheme

@Composable
fun NewBattleScreen(
    onBackClick: ()->Unit,
) {
    Text(
        text = "뉴 배틀이지롱",
        style = SwypTheme.typography.h1SemiBold,
    )
}
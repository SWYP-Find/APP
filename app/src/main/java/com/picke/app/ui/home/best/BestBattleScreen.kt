package com.picke.app.ui.home.best

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.picke.app.ui.theme.SwypTheme

@Composable
fun BestBattleScreen(
    onBackClick: ()->Unit,
) {
    Text(
        text = "베스트 배틀이지롱",
        style = SwypTheme.typography.h1SemiBold,
    )
}
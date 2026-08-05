package com.picke.presentation.ui.home.best

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.picke.presentation.ui.theme.PickeTheme

@Composable
fun BestBattleScreen(
    onBackClick: ()->Unit,
) {
    Text(
        text = "베스트 배틀이지롱",
        style = PickeTheme.typography.h1SemiBold,
    )
}
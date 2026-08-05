package com.picke.presentation.ui.home.new

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.picke.presentation.ui.theme.PickeTheme

@Composable
fun NewBattleScreen(
    onBackClick: ()->Unit,
) {
    Text(
        text = "뉴 배틀이지롱",
        style = PickeTheme.typography.h1SemiBold,
    )
}
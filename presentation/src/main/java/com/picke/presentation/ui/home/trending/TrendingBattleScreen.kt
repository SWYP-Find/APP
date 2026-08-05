package com.picke.presentation.ui.home.trending

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.picke.presentation.ui.theme.PickeTheme

@Composable
fun TrendingBattleScreen(
    onBackClick: ()->Unit,
) {
    Text(
        text = "트렌드 배틀이지롱",
        style = PickeTheme.typography.h1SemiBold,
    )
}
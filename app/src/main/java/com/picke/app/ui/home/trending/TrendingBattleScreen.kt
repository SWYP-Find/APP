package com.picke.app.ui.home.trending

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.picke.app.ui.theme.SwypTheme

@Composable
fun TrendingBattleScreen(
    onBackClick: ()->Unit,
) {
    Text(
        text = "트렌드 배틀이지롱",
        style = SwypTheme.typography.h1SemiBold,
    )
}
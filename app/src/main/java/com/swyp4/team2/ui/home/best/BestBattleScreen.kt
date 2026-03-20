package com.swyp4.team2.ui.home.best

import android.R
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.swyp4.team2.ui.theme.SwypTheme

@Composable
fun BestBattleScreen(
    onBackClick: ()->Unit,
) {
    Text(
        text = "베스트 배틀이지롱",
        style = SwypTheme.typography.h1SemiBold,
    )
}
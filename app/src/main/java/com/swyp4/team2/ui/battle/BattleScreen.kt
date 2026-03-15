package com.swyp4.team2.ui.battle

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.SwypTheme

@Composable
fun BattleScreen(){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "오늘의 배틀이지롱",
            style = SwypTheme.typography.b1Medium,
            color = Gray900,
        )
    }
}
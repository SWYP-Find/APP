package com.swyp4.team2.ui.my.philosopher

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.swyp4.team2.ui.component.CustomTopAppBar
import com.swyp4.team2.ui.theme.SwypTheme

@Composable
fun PhilosopherTypeScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar={
            CustomTopAppBar(
                title = "나는 어떤 철학자일까?",
                showLogo = false,
                showBackButton = true,
                onBackClick = {onBackClick()},
                backgroundColor = SwypTheme.colors.background
            )
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ){

        }
    }
}
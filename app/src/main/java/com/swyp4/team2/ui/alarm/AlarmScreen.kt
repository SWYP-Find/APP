package com.swyp4.team2.ui.alarm

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.swyp4.team2.R
import com.swyp4.team2.ui.component.CustomTopAppBar
import com.swyp4.team2.ui.theme.SwypTheme

@Composable
fun AlarmScreen(
    onBackClick:()->Unit
) {
    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        topBar={
            CustomTopAppBar(
                title = stringResource(R.string.alarm),
                showLogo = false,
                showBackButton = true,
                onBackClick = { onBackClick() },
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
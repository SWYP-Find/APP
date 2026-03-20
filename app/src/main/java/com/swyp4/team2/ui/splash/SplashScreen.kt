package com.swyp4.team2.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swyp4.team2.R
import com.swyp4.team2.ui.theme.Primary500
import com.swyp4.team2.ui.theme.SwypAppTheme
import com.swyp4.team2.ui.theme.SwypTheme

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onNavigateToLogin: ()->Unit,
    onNavigateToOnboarding: () -> Unit,
    onNavigateToMain: ()->Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        when(uiState){
            SplashUiState.NavigateToLogin -> onNavigateToLogin()
            SplashUiState.NavigateToOnboarding -> onNavigateToOnboarding()
            SplashUiState.NavigateToMain -> onNavigateToMain()
            SplashUiState.Loading -> { } // 로고 띄우기
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF893825)),
        contentAlignment = Alignment.Center
    ){
        Image(
            painter = painterResource(id = R.drawable.ic_splash_logo),
            contentDescription = "로고 이미지",
            modifier = Modifier.size(200.dp)
        )
    }
}
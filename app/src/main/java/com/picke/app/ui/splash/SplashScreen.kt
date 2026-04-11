package com.picke.app.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.picke.app.R
import com.picke.app.ui.theme.Primary500
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onNavigateToLogin: ()->Unit,
    onNavigateToOnboarding: () -> Unit,
    onNavigateToMain: ()->Unit,
    onNavigateToOtherPhilosopher: (String) -> Unit,
    onNavigateToBattle: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val scale = remember { Animatable(2f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        launch {
            // 서서히 나타나기 (Fade-in: 0.5초 동안)
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 500)
            )
        }
        launch {
            // 쾅! 하고 도장 찍히기 (Scale-down with bounce)
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
    }

    LaunchedEffect(uiState) {
        when(val state = uiState){
            is SplashUiState.NavigateToLogin -> onNavigateToLogin()
            is SplashUiState.NavigateToOnboarding -> onNavigateToOnboarding()
            is SplashUiState.NavigateToMain -> onNavigateToMain()
            is SplashUiState.NavigateToOtherPhilosopher -> onNavigateToOtherPhilosopher(state.reportId)
            is SplashUiState.NavigateToBattle -> onNavigateToBattle(state.battleId)
            is SplashUiState.Loading -> { }
        }
    }

    // 중앙: 로고
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary500),
        contentAlignment = Alignment.Center
    ){
        Image(
            painter = painterResource(id = R.drawable.symbol_picke),
            contentDescription = "Picke Logo",
            modifier = Modifier.size(200.dp)
                .scale(scale.value)
                .alpha(alpha.value)
        )
    }
}
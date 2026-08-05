package com.picke.presentation.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme

@Preview(showSystemUi = true, name = "Splash")
@Composable
private fun SplashScreenPreview() {
    PickeTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PickeTheme.colors.primary),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_picke_splash),
                contentDescription = "Picke Logo",
                modifier = Modifier.size(200.dp)
            )
        }
    }
}

/*
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
            .background(SwypTheme.colors.primary),
        contentAlignment = Alignment.Center
    ){
        Image(
            painter = painterResource(id = R.drawable.logo_picke_splash),
            contentDescription = "Picke Logo",
            modifier = Modifier.size(200.dp)
                .scale(scale.value)
                .alpha(alpha.value)
        )
    }
}*/

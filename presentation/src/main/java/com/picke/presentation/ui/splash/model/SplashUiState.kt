package com.picke.presentation.ui.splash.model

sealed class SplashUiState {

    object Loading : SplashUiState()

    object NavigateToLogin : SplashUiState()

    object NavigateToOnboarding : SplashUiState()

    data class NavigateToMain(val needsTermsAgreement: Boolean = false) : SplashUiState()

    data class NavigateToOtherPhilosopher(
        val reportId: String,
        val needsTermsAgreement: Boolean = false
    ) : SplashUiState()

    data class NavigateToBattle(
        val battleId: String,
        val needsTermsAgreement: Boolean = false
    ) : SplashUiState()
}
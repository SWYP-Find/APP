package com.picke.presentation.ui.login.model

sealed class LoginUiState {

    object Idle : LoginUiState()

    object Loading : LoginUiState()

    data class Success(
        val isNewUser: Boolean,
        val needsTermsAgreement: Boolean
    ) : LoginUiState()

    data class Error(val message: String) : LoginUiState()
}
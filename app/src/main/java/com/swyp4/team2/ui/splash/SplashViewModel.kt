package com.swyp4.team2.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swyp4.team2.data.local.TokenManager
import com.swyp4.team2.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SplashUiState{
    object Loading : SplashUiState()
    object NavigateToLogin : SplashUiState() // 소셜로그인
    object NavigateToOnboarding : SplashUiState() // 온보딩
    object NavigateToMain : SplashUiState() // 홈화면
}
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModel() {
    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    init {
        tokenManager.clearAll() //TODO 소셜 로그인 구현 되면 없애야 함
        checkAutoLogin()
    }

    private fun checkAutoLogin() {
        viewModelScope.launch {
            delay(1500L)

            val localRefreshToken = tokenManager.getRefreshToken()

            if (localRefreshToken.isNullOrBlank()) {
                // 토큰이 없으면 온보딩 화면으로
                _uiState.value = SplashUiState.NavigateToOnboarding
            } else {
                val result = authRepository.refreshAccessToken(localRefreshToken)

                result.onSuccess {
                    _uiState.value = SplashUiState.NavigateToMain
                }.onFailure {
                    tokenManager.clearAll()
                    _uiState.value = SplashUiState.NavigateToLogin
                }
            }
        }
    }
}
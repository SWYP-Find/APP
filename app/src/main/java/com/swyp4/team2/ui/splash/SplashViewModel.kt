package com.swyp4.team2.ui.splash

import android.util.Log
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
        checkAutoLogin()
    }

    private fun checkAutoLogin() {
        viewModelScope.launch {
            delay(1000)

            val localRefreshToken = tokenManager.getRefreshToken()
            val localAccessToken = tokenManager.getAccessToken()

            Log.d("TokenFlow", "🔥 AccessToken: $localAccessToken")
            Log.d("TokenFlow", "🔥 RefreshToken: $localRefreshToken")
            Log.d("SplashFlow", "1. 저장된 리프레시 토큰: $localRefreshToken")

            if (localRefreshToken.isNullOrBlank()) {
                Log.d("SplashFlow", "2. 토큰 없음 -> 신규 유저 판단 (온보딩 이동)")
                _uiState.value = SplashUiState.NavigateToOnboarding
            } else {
                Log.d("SplashFlow", "2. 토큰 있음 -> 기존 유저 판단 (토큰 갱신 시도)")
                val result = authRepository.refreshAccessToken(localRefreshToken)

                result.onSuccess {
                    Log.d("SplashFlow", "3. 토큰 갱신 성공! -> 홈(Main) 화면 이동")
                    _uiState.value = SplashUiState.NavigateToMain
                }.onFailure { error ->
                    Log.e("SplashFlow", "3. 토큰 갱신 실패! -> 로그인 화면 이동", error)
                    tokenManager.clearAll()
                    _uiState.value = SplashUiState.NavigateToLogin
                }
            }
        }
    }
}
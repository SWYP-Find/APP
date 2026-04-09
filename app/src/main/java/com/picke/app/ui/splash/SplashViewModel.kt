package com.picke.app.ui.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.app.data.local.TokenManager
import com.picke.app.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SplashUiState{
    object Loading : SplashUiState() // 로딩중
    object NavigateToLogin : SplashUiState() // 소셜로그인
    object NavigateToOnboarding : SplashUiState() // 온보딩
    object NavigateToMain : SplashUiState() // 메인화면
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModel() {
    companion object {
        private const val TAG = "SplashViewModel_Picke"
    }

    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    init {
        checkAutoLogin()
    }

    private fun checkAutoLogin() {
        viewModelScope.launch {
            Log.d(TAG, "[FLOW] 자동 로그인 체크 시작")
            delay(1000)

            val localRefreshToken = tokenManager.getRefreshToken()

            if (localRefreshToken.isNullOrBlank()) {
                Log.i(TAG, "[NAV] 토큰 없음: 신규 유저로 판단 -> 온보딩")
                _uiState.value = SplashUiState.NavigateToOnboarding
            } else {
                Log.d(TAG, "[STATE] 기존 토큰 발견: 서버 확인 절차 진입")
                val result = authRepository.refreshAccessToken(localRefreshToken)

                result.onSuccess {
                    Log.i(TAG, "[NAV] 인증 성공: 메인 화면")
                    _uiState.value = SplashUiState.NavigateToMain
                }.onFailure { error ->
                    Log.w(TAG, "[NAV] 인증 실패: 로그인 화면")
                    tokenManager.clearAll()
                    _uiState.value = SplashUiState.NavigateToLogin
                }
            }
        }
    }
}
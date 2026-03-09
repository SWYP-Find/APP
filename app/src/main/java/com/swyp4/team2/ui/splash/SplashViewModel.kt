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
    object NavigateToMain : SplashUiState() // 홈화면
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModel() {
    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    init{
        checkAutoLogin()
    }

    private fun checkAutoLogin(){
        viewModelScope.launch{
            delay(1500)
            val refreshToken = tokenManager.getRefreshToken()

            if(refreshToken.isNullOrEmpty()){
                // 경우1. 저장된 refreshToken이 없음 -> 소셜 로그인 화면으로 이동
                _uiState.value = SplashUiState.NavigateToLogin
            } else {
                val result = authRepository.refreshAccessToken(refreshToken)

                result.onSuccess{
                    // 경우3. refreshToken 유효함 -> 홈 화면으로 이동
                    _uiState.value = SplashUiState.NavigateToMain
                }.onFailure{
                    // 경우2. refreshToken 만료됨 -> 소셜 로그인 화면으로 이동
                    _uiState.value = SplashUiState.NavigateToLogin
                }
            }
        }
    }
}
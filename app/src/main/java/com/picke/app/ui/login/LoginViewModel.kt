package com.picke.app.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.app.BuildConfig
import com.picke.app.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState() // 로딩중
    data class Success(val isNewUser: Boolean) : LoginUiState() // 로그인 성공
    data class Error(val message: String) : LoginUiState() // 에러 발생
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    companion object {
        private const val TAG = "LoginViewModel_Picke"
    }

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState : StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }

    fun handleSocialLoginSuccess(provider: String, authCode: String) {
        // API 중복 요청 방지
        if (_uiState.value is LoginUiState.Loading) {
            Log.d(TAG, "[FLOW] 중복 로그인 요청 차단")
            return
        }

        _uiState.value = LoginUiState.Loading

        viewModelScope.launch {
            Log.d(TAG, "[FLOW] $provider 인가 코드 획득 완료. AuthCode: $authCode")

            val redirectUri = when (provider) {
                "kakao" -> "kakao${BuildConfig.KAKAO_DEBUG_APPKEY}://oauth"
                "google" -> "https://picke.store/oauth/google"
                else -> ""
            }
            Log.d("LoginFlow", "[STATE] Redirect URI 결정: $redirectUri")

            val result = authRepository.login(
                provider = provider,
                authCode = authCode,
                redirectUri = redirectUri
            )

            result.onSuccess { authToken ->
                Log.i(TAG, "[NAV] ${provider} 로그인 성공 (신규 유저 여부: ${authToken.isNewUser})")
                _uiState.value = LoginUiState.Success(isNewUser = authToken.isNewUser)
            }.onFailure { error ->
                Log.w(TAG, "[FLOW] ${provider} 로그인 실패: ${error.message}")
                _uiState.value = LoginUiState.Error(error.message ?: "로그인에 실패했습니다.")
            }
        }
    }
}

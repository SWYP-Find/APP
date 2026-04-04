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

// 화면 상태 정의
sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val isNewUser: Boolean) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState : StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }

    fun handleSocialLoginSuccess(provider: String, authCode: String) {
        // 1. 방어막: 현재 로딩 중이면 튕겨냅니다.
        if (_uiState.value is LoginUiState.Loading) {
            Log.d("LoginFlow", "🚨 이미 로그인 처리 중입니다! 중복 호출 방어 성공!")
            return
        }

        _uiState.value = LoginUiState.Loading

        viewModelScope.launch {
            Log.d("LoginFlow", "1. [$provider] 인가 코드 획득 완료! 백엔드에 로그인 요청 시작. (AuthCode: $authCode)")

            val redirectUri = when (provider) {
                "kakao" -> "kakao${BuildConfig.KAKAO_DEBUG_APPKEY}://oauth"
                "google" -> "https://picke.store/oauth/google"
                else -> ""
            }
            Log.d("LoginFlow", "2. 전송할 Redirect URI: $redirectUri")

            val result = authRepository.login(
                provider = provider,
                authCode = authCode,
                redirectUri = redirectUri
            )

            result.onSuccess { authToken ->
                Log.d("LoginFlow", "3. 🟢 백엔드 로그인 통신 성공! (신규 유저 여부: ${authToken.isNewUser})")
                _uiState.value = LoginUiState.Success(isNewUser = authToken.isNewUser)
            }.onFailure { error ->
                Log.e("LoginFlow", "3. 🔴 백엔드 로그인 통신 실패!", error)
                _uiState.value = LoginUiState.Error(error.message ?: "로그인에 실패했습니다.")
            }
        }
    }
}

package com.picke.app.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
        if (_uiState.value is LoginUiState.Loading) return

        viewModelScope.launch {
            Log.d("LoginFlow", "1. [$provider] 인가 코드 획득 완료! 백엔드에 로그인 요청 시작. (AuthCode: $authCode)")
            _uiState.value = LoginUiState.Loading

            /*val redirectUri = if (provider == "kakao") {
                "kakao${BuildConfig.KAKAO_DEBUG_APPKEY}://oauth"
            } else {
                "" // 구글은 보통 안드로이드 클라이언트에서 빈 문자열이거나 특정 설정을 따릅니다.
            }*/

            val redirectUri = when (provider) {
                "kakao" -> "https://picke.store/oauth/kakao"
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
                Log.d("LoginFlow", "$result")
                Log.d("LoginFlow", "3. 🟢 백엔드 로그인 통신 성공! (신규 유저 여부: ${authToken.isNewUser})")
                _uiState.value = LoginUiState.Success(isNewUser = authToken.isNewUser)
            }.onFailure { error ->
                Log.e("LoginFlow", "3. 🔴 백엔드 로그인 통신 실패!", error.message as Throwable?)
                Log.d("LoginFlow", "$result")
                _uiState.value = LoginUiState.Error(error.message ?: "로그인에 실패했습니다.")
            }
        }
    }
}

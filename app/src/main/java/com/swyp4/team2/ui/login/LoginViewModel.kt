package com.swyp4.team2.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swyp4.team2.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.swyp4.team2.BuildConfig

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

    fun handleSocialLoginSuccess(provider: String, authCode: String) {
        viewModelScope.launch {
            Log.d("LoginFlow", "1. [$provider] 인가 코드 획득 완료! 백엔드에 로그인 요청 시작. (AuthCode: $authCode)")
            _uiState.value = LoginUiState.Loading

            // TODO: 서비스에 맞는 리다이렉트 URI를 넣어야 합니다. (보통 카카오는 개발자 센터에 등록한 URI)
            val redirectUri = if (provider == "kakao") {
                "kakao${BuildConfig.KAKAO_DEBUG_APPKEY}://oauth"
            } else {
                "" // 구글은 보통 안드로이드 클라이언트에서 빈 문자열이거나 특정 설정을 따릅니다.
            }
            Log.d("LoginFlow", "2. 전송할 Redirect URI: $redirectUri")

            val result = authRepository.login(
                provider = provider,
                authCode = authCode,
                redirectUri = redirectUri
            )

            result.onSuccess { authToken ->
                // 🌟 4. 성공 시, 이 유저가 신규 유저인지 정보를 Success 상태에 실어 보냅니다.
                // TokenManager 저장 로직은 이미 RepositoryImpl에 넣어두었으니 여기선 상태만 넘기면 끝!
                Log.d("LoginFlow", "3. 🟢 백엔드 로그인 통신 성공! (신규 유저 여부: ${authToken.isNewUser})")
                _uiState.value = LoginUiState.Success(isNewUser = authToken.isNewUser)
            }.onFailure { error ->
                Log.e("LoginFlow", "3. 🔴 백엔드 로그인 통신 실패!", error)
                _uiState.value = LoginUiState.Error(error.message ?: "로그인에 실패했습니다.")
            }
        }
    }
}

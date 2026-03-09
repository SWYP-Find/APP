package com.swyp4.team2.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swyp4.team2.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// 화면 상태 정의
sealed class LoginUiState{
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState : StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun handleSocialLoginSuccess(provider: String, token: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading

            val result = authRepository.loginWithSocialToken(provider, token)

            result.onSuccess { authToken ->
                // TODO: 여기서 받아온 자체 토큰(authToken)을 TokenManager 등에 저장
                // 여기서 authToken.isNewUser 값에 따라 온보딩으로 갈지, 메인으로 갈지 상태를 나눌 수도 있습니다!
                _uiState.value = LoginUiState.Success
            }.onFailure { error ->
                _uiState.value = LoginUiState.Error(error.message ?: "로그인에 실패했습니다.")
            }
        }
    }
}

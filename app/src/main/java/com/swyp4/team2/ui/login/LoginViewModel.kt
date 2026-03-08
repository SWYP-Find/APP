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

    // UI에서 카카오 SDK를 통해 얻어온 토큰을 이 함수로 넘겨준다.
    fun handleKakaoLoginSuccess(kakaoAccessToken: String){
        viewModelScope.launch{
            _uiState.value = LoginUiState.Loading

            val result = authRepository.loginWithKakaoToken(kakaoAccessToken)

            result.onSuccess { token ->
                // TODO: 여기서 받아온 자체 토큰(token)을 SP에 저장
                _uiState.value = LoginUiState.Success
            }.onFailure { error->
                _uiState.value = LoginUiState.Error(error.message ?: "로그인에 실패했습니다.")
            }
        }
    }
}

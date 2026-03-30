package com.swyp4.team2.ui.my.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swyp4.team2.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingUiState(
    val isLoading: Boolean = false,
    val navigateToLogin: Boolean = false, // 로그아웃이나 탈퇴 완료 후 로그인 창으로 보낼 플래그
    val errorMessage: String? = null
)

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingUiState())
    val uiState: StateFlow<SettingUiState> = _uiState.asStateFlow()

    // 로그아웃
    fun logout() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = authRepository.logout()

            _uiState.update {
                it.copy(
                    isLoading = false,
                    navigateToLogin = true
                )
            }
        }
    }

    // 회원 탈퇴
    fun withdraw() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = authRepository.withdraw()

            _uiState.update {
                it.copy(
                    isLoading = false,
                    navigateToLogin = true
                )
            }
        }
    }
}
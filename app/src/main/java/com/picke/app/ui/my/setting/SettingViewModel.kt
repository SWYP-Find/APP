package com.picke.app.ui.my.setting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.app.domain.repository.AuthRepository
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
    private val TAG = "SettingFlow"

    // 1. 로그아웃
    fun logout() {
        Log.d(TAG, "▶️ [로그아웃] 프로세스 시작")
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = authRepository.logout()
            Log.d(TAG, "➡️ [로그아웃] 결과 수신: $result")

            result.onSuccess {
                Log.i(TAG, "✅ [로그아웃] 성공! 로컬 토큰 삭제 및 로그인 화면으로 이동합니다.")
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        navigateToLogin = true
                    )
                }
            }.onFailure { error ->
                Log.e(TAG, "❌ [로그아웃] 실패: ${error.message}", error)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "로그아웃 중 오류가 발생했습니다: ${error.localizedMessage}"
                    )
                }
            }
        }
    }

    // 2. 회원 탈퇴
    fun withdraw() {
        // 명세서에 따른 탈퇴 사유 (필요에 따라 UI에서 선택받게 바꿀 수 있어요!)
        val withdrawalReason = "NOT_USED_OFTEN"

        Log.d(TAG, "▶️ [회원탈퇴] 프로세스 시작 (사유: $withdrawalReason)")

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // Repository에서 'reason'을 받도록 인터페이스를 수정했다는 전제하에 보냅니다.
            val result = authRepository.withdraw(reason = withdrawalReason)
            Log.d(TAG, "➡️ [회원탈퇴] 서버 응답 결과: $result")

            result.onSuccess {
                Log.i(TAG, "✅ [회원탈퇴] 성공! 서버 연동 해제 및 데이터 파기 완료.")
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        navigateToLogin = true
                    )
                }
            }.onFailure { error ->
                Log.e(TAG, "❌ [회원탈퇴] 서버 통신 실패 (HTTP 400 등이 발생했을 수 있음)", error)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "탈퇴 요청에 실패했습니다. 잠시 후 다시 시도해주세요."
                    )
                }
            }
        }
    }
}
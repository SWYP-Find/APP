package com.picke.app.ui.my.philosopher

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.app.domain.model.MyRecapBoard
import com.picke.app.domain.repository.MyPageRepository
import com.picke.app.domain.repository.ShareRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PhilosopherTypeUiState(
    val recapBoard: MyRecapBoard? = null,
    val isLoading: Boolean = false,
    val isLocked: Boolean = false
)

@HiltViewModel
class PhilosopherTypeViewModel @Inject constructor(
    private val myPageRepository: MyPageRepository,
    private val shareRepository: ShareRepository
) : ViewModel(){

    companion object {
        private const val TAG = "PhilosopherTypeVM_Picke"
    }

    private val _uiState = MutableStateFlow(PhilosopherTypeUiState())
    val uiState: StateFlow<PhilosopherTypeUiState> = _uiState.asStateFlow()

    // 나의 철학자 유형 정보 가져오기
    fun fetchPhilosopherReport() {
        viewModelScope.launch {
            Log.d(TAG, "[FLOW] 철학자 리포트 데이터 요청 시작")

            // 1. 통신 시작 전 로딩 상태 On
            _uiState.update { it.copy(isLoading = true) }

            // 2. 서버에 데이터 요청
            val result = myPageRepository.getMyRecap()

            // 3. 통신 결과에 따른 분기 처리
            result.onSuccess { data ->
                Log.i(TAG, "[STATE] 리포트 데이터 로드 성공: 화면 잠금 해제")
                _uiState.update {
                    it.copy(
                        recapBoard = data,
                        isLoading = false,
                        isLocked = false
                    )
                }
            }.onFailure { exception ->
                Log.w(TAG, "[STATE] 리포트 로드 실패 또는 접근 조건 미달: 잠금 화면 처리 (사유: ${exception.message})")
                _uiState.update {
                    it.copy(
                        recapBoard = null,
                        isLoading = false,
                        isLocked = true
                    )
                }
            }
        }
    }

    // 타인의 철학자 유형 정보 가져오기

    fun fetchOtherPhilosopherReport(shareKey: String) {
        viewModelScope.launch {
            Log.d(TAG, "[FLOW] 타인의 철학자 리포트 데이터 요청 시작 (shareKey: $shareKey)")
            _uiState.update { it.copy(isLoading = true) }

            val result = shareRepository.getRecapDetail(shareKey)

            result.onSuccess { data ->
                Log.i(TAG, "[STATE] 타인 리포트 데이터 로드 성공")
                _uiState.update {
                    it.copy(
                        recapBoard = data,
                        isLoading = false,
                        isLocked = false
                    )
                }
            }.onFailure { exception ->
                Log.e(TAG, "[STATE] 타인 리포트 로드 실패: ${exception.message}")
                _uiState.update {
                    it.copy(
                        recapBoard = null,
                        isLoading = false,
                        isLocked = true
                    )
                }
            }
        }
    }

    // 나의 철학자 유형 공유키 발급받기 (기존 getShareLink 교체)
    fun getRecapShareKey(onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            Log.d(TAG, "[FLOW] 리캡 공유 키 발급 요청 시작")
            val result = shareRepository.getRecapShareKey()

            result.onSuccess { shareKeyData ->
                val key = shareKeyData.shareKey
                Log.i(TAG, "[STATE] 리캡 공유 키 발급 성공: $key")
                onSuccess(key)
            }.onFailure { exception ->
                Log.e(TAG, "[STATE] 공유 키 발급 실패: ${exception.message}")
                onError(exception.message ?: "공유 링크를 생성하지 못했습니다.")
            }
        }
    }
}
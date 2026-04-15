package com.picke.app.ui.my.point

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.app.domain.model.CreditHistoryItem
import com.picke.app.domain.repository.MyPageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PointUiState(
    val pointList: List<PointHistoryUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val nextOffset: Int? = null,
    val hasNext: Boolean = true
)

@HiltViewModel
class PointViewModel @Inject constructor(
    private val myPageRepository: MyPageRepository
) : ViewModel() {

    companion object {
        private const val TAG = "PointViewModel"
    }

    private val _uiState = MutableStateFlow(PointUiState())
    val uiState: StateFlow<PointUiState> = _uiState.asStateFlow()

    init {
        loadPointHistory(isRefresh = true)
    }

    fun loadPointHistory(isRefresh: Boolean = false) {
        val currentState = _uiState.value

        if (currentState.isLoading || (!isRefresh && !currentState.hasNext)) return

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val offset = if (isRefresh) null else currentState.nextOffset

            Log.d(TAG, "포인트 내역 요청 시작 - offset: $offset")

            myPageRepository.getCreditHistory(offset = offset, size = 15)
                .onSuccess { page ->
                    Log.d(TAG, "포인트 내역 성공 - 가져온 개수: ${page.items.size}")

                    val newUiItems = page.items.map { it.toUiModel() }

                    _uiState.update { state ->
                        state.copy(
                            // 새로고침이면 덮어쓰고, 아니면 기존 리스트에 추가
                            pointList = if (isRefresh) newUiItems else state.pointList + newUiItems,
                            nextOffset = page.nextOffset,
                            hasNext = page.hasNext,
                            isLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    Log.e(TAG, "포인트 내역 실패: ${error.message}", error)
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }
}

private fun CreditHistoryItem.toUiModel(): PointHistoryUiModel {
    val title = when (this.creditType) {
        "FREE_CHARGE" -> "무료 충전"
        "TOPIC_SUGGEST" -> "배틀 제안"
        "BATTLE_ENTRY" -> "배틀 참여"
        "BEST_COMMENT" -> "베스트 댓글 보상"
        "BATTLE_PROPOSAL_ACCEPTED" -> "제안 배틀 채택"
        "MAJORITY_WIN" -> "다수결 보상"
        "WEEKLY_CHARGE" -> "자동 충전"
        "SIGNUP_REWARD" -> "가입 축하 포인트"
        else -> "포인트 내역"
    }

    return PointHistoryUiModel(
        title = title,
        date = this.createdAt,
        point = this.amount,
        type = if (this.amount > 0) "적립" else "사용"
    )
}
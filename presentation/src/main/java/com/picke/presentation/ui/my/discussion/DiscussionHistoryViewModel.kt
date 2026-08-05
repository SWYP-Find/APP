package com.picke.presentation.ui.my.discussion

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.feature.mypage.model.MyBattleRecordItem
import com.picke.domain.feature.mypage.usecase.MyPageUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DiscussionHistoryUiState(
    val items: List<MyBattleRecordItem> = emptyList(),
    val nextOffset: Int? = 0,
    val hasMore: Boolean = true,
    val isLoading: Boolean = false,
    val isPagingLoading: Boolean = false
)

@HiltViewModel
class DiscussionHistoryViewModel @Inject constructor(
    private val myPageUseCases: MyPageUseCases
) : ViewModel() {
    private val _uiState = MutableStateFlow(DiscussionHistoryUiState())
    val uiState: StateFlow<DiscussionHistoryUiState> = _uiState.asStateFlow()

    private val TAG = "DiscussionHistoryFlow"

    init {
        fetchDiscussionHistory()
    }

    private fun fetchDiscussionHistory() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result =
                myPageUseCases.getMyBattleRecordsUseCase(offset = 0, size = 20, voteSide = null)

            result.onSuccess { data ->
                Log.d(
                    TAG,
                    "배틀 기록 불러오기 성공! 아이템 개수: ${data.items.size}, nextOffset: ${data.nextOffset}"
                )
            }.onFailure { exception ->
                Log.e(TAG, "배틀 기록 불러오기 실패: ${exception.message}")
            }

            _uiState.update { state ->
                state.copy(
                    items = result.getOrNull()?.items ?: emptyList(),
                    nextOffset = result.getOrNull()?.nextOffset,
                    hasMore = result.getOrNull()?.hasNext ?: false,
                    isLoading = false
                )
            }
        }
    }

    fun loadMore() {
        val currentState = _uiState.value
        if (currentState.isPagingLoading) return
        if (!currentState.hasMore || currentState.nextOffset == null) return

        viewModelScope.launch {
            _uiState.update { it.copy(isPagingLoading = true) }
            Log.d(TAG, "페이징 시작: offset=${currentState.nextOffset}")

            val result = myPageUseCases.getMyBattleRecordsUseCase(
                offset = currentState.nextOffset,
                size = 20,
                voteSide = null
            )

            result.onSuccess { pageData ->
                Log.d(TAG, "페이징 성공: 추가된 개수=${pageData.items.size}")
                _uiState.update { state ->
                    state.copy(
                        items = state.items + pageData.items,
                        nextOffset = pageData.nextOffset,
                        hasMore = pageData.hasNext,
                        isPagingLoading = false
                    )
                }
            }.onFailure { exception ->
                Log.e(TAG, "페이징 실패: 에러=${exception.message}")
                _uiState.update { it.copy(isPagingLoading = false) }
            }
        }
    }
}

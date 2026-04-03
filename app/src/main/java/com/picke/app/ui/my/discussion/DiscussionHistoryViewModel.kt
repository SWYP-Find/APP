package com.picke.app.ui.my.discussion

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.app.domain.model.MyBattleRecordItem
import com.picke.app.domain.repository.MyPageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DiscussionHistoryUiState(
    val agreeList: List<MyBattleRecordItem> = emptyList(),
    val agreeOffset: Int? = 0,
    val hasMoreAgree: Boolean = true,
    val category: String = "",

    val disagreeList: List<MyBattleRecordItem> = emptyList(),
    val disagreeOffset: Int? = 0,
    val hasMoreDisagree: Boolean = true,

    val isLoading: Boolean = false,
    val isPagingLoading: Boolean = false
)

@HiltViewModel
class DiscussionHistoryViewModel @Inject constructor(
    private val myPageRepository: MyPageRepository
) : ViewModel(){
    private val _uiState = MutableStateFlow(DiscussionHistoryUiState())
    val uiState: StateFlow<DiscussionHistoryUiState> = _uiState.asStateFlow()

    private val TAG = "DiscussionHistoryFlow"

    init {
        fetchDiscussionHistory()
    }
    private fun fetchDiscussionHistory() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            Log.d(TAG, "fetchDiscussionHistory: 통신 시작")

            val agreeDeferred = async {
                myPageRepository.getMyBattleRecords(offset = 0, size = 20, voteSide = "PRO")
            }
            val disagreeDeferred = async {
                myPageRepository.getMyBattleRecords(offset = 0, size = 20, voteSide = "CON")
            }

            val agreeResult = agreeDeferred.await()
            val disagreeResult = disagreeDeferred.await()

            agreeResult.onSuccess { data ->
                Log.d(TAG, "찬성 기록 불러오기 성공! 아이템 개수: ${data.items.size}, nextOffset: ${data.nextOffset}")
            }.onFailure { exception ->
                Log.e(TAG, "찬성 기록 불러오기 실패: ${exception.message}")
            }

            disagreeResult.onSuccess { data ->
                Log.d(TAG, "반대 기록 불러오기 성공! 아이템 개수: ${data.items.size}, nextOffset: ${data.nextOffset}")
            }.onFailure { exception ->
                Log.e(TAG, "반대 기록 불러오기 실패: ${exception.message}")
            }

            _uiState.update { state ->
                state.copy(
                    agreeList = agreeResult.getOrNull()?.items ?: emptyList(),
                    agreeOffset = agreeResult.getOrNull()?.nextOffset,
                    hasMoreAgree = agreeResult.getOrNull()?.hasNext ?: false,
                    disagreeList = disagreeResult.getOrNull()?.items ?: emptyList(),
                    disagreeOffset = disagreeResult.getOrNull()?.nextOffset,
                    hasMoreDisagree = disagreeResult.getOrNull()?.hasNext ?: false,

                    isLoading = false
                )
            }
        }
    }

    fun loadMore(voteSide: String) {
        val currentState = _uiState.value

        if (currentState.isPagingLoading) return

        val (currentOffset, hasNext) = if (voteSide == "PRO") {
            currentState.agreeOffset to currentState.hasMoreAgree
        } else {
            currentState.disagreeOffset to currentState.hasMoreDisagree
        }

        if (!hasNext || currentOffset == null) return

        viewModelScope.launch {
            _uiState.update { it.copy(isPagingLoading = true) }
            Log.d(TAG, "페이징 시작: voteSide=$voteSide, offset=$currentOffset")

            val result = myPageRepository.getMyBattleRecords(
                offset = currentOffset,
                size = 20,
                voteSide = voteSide
            )

            result.onSuccess { pageData ->
                Log.d(TAG, "페이징 성공: voteSide=$voteSide, 추가된 개수=${pageData.items.size}")
                _uiState.update { state ->
                    if (voteSide == "PRO") {
                        state.copy(
                            agreeList = state.agreeList + pageData.items,
                            agreeOffset = pageData.nextOffset,
                            hasMoreAgree = pageData.hasNext,
                            isPagingLoading = false,
                        )
                    } else {
                        state.copy(
                            disagreeList = state.disagreeList + pageData.items,
                            disagreeOffset = pageData.nextOffset,
                            hasMoreDisagree = pageData.hasNext,
                            isPagingLoading = false
                        )
                    }
                }
            }.onFailure { exception ->
                Log.e(TAG, "페이징 실패: voteSide=$voteSide, 에러=${exception.message}")
                _uiState.update { it.copy(isPagingLoading = false) }
            }
        }
    }
}
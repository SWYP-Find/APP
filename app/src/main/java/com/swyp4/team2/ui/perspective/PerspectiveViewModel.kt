package com.swyp4.team2.ui.perspective

import android.util.Log // 🌟 로그를 위해 추가
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swyp4.team2.domain.repository.PerspectiveRepository
import com.swyp4.team2.domain.repository.VoteRepository
import com.swyp4.team2.ui.perspective.model.PerspectiveUiModel
import com.swyp4.team2.ui.perspective.model.mockPerspectiveList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "PerspectiveFlow"

data class PerspectiveUiState(
    val battleId: String = "",
    val proRatio: Float = 0.5f,
    val conRatio: Float = 0.5f,
    val perspectives: List<PerspectiveUiModel> = emptyList(),
    val nextCursor: String? = null,
    val hasNext: Boolean = true,
    val isLoading: Boolean = false
)

@HiltViewModel
class PerspectiveViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val perspectiveRepository: PerspectiveRepository,
    private val voteRepository: VoteRepository
): ViewModel() {

    private val receivedBattleId: String = checkNotNull(savedStateHandle["battleId"])

    private val _uiState = MutableStateFlow(
        PerspectiveUiState(
            battleId = receivedBattleId,
            perspectives = mockPerspectiveList,
            hasNext = true
        )
    )
    val uiState: StateFlow<PerspectiveUiState> = _uiState.asStateFlow()

    init {
        Log.d(TAG, "ViewModel 생성됨 - 전달받은 battleId: $receivedBattleId")
        loadPerspectives()
        loadVoteStats()
    }

    private fun loadVoteStats() {
        viewModelScope.launch {
            Log.d(TAG, "투표 통계(비율) 호출 시작 - battleId: ${_uiState.value.battleId}")

            voteRepository.getVoteStats(_uiState.value.battleId)
                .onSuccess { statsBoard ->
                    val pro = statsBoard.options.find { it.label == "A" }?.ratio ?: 0.5f
                    val con = statsBoard.options.find { it.label == "B" }?.ratio ?: 0.5f

                    // 🟢 성공 로그: A와 B의 비율이 제대로 계산되었는지 확인
                    Log.d(TAG, "🟢 투표 통계 조회 성공 - A비율: $pro, B비율: $con")

                    _uiState.update {
                        it.copy(proRatio = pro, conRatio = con)
                    }
                }
                .onFailure { error ->
                    Log.e(TAG, "🔴 투표 통계 조회 실패: ${error.message}", error)
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }

    fun loadPerspectives(isRefresh: Boolean = false) {
        val state = _uiState.value
        if (state.isLoading || (!isRefresh && !state.hasNext)) return

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val cursor = if (isRefresh) null else state.nextCursor
            val battleIdLong = receivedBattleId.toLongOrNull() ?: 0L

            Log.d(TAG, "관점(댓글) 목록 호출 시작 - battleId: $battleIdLong, cursor: $cursor")

            perspectiveRepository.getPerspectives(battleId = battleIdLong, cursor = cursor, size = 10)
                .onSuccess { page ->
                    Log.d(TAG, "🟢 관점 목록 조회 성공 - 가져온 개수: ${page.items.size}, hasNext: ${page.hasNext}")

                    // val newItems = page.items.map { it.toUiModel() } // TODO: 매퍼 연결
                    val newItems = emptyList<PerspectiveUiModel>()

                    _uiState.update {
                        it.copy(
                            perspectives = if (isRefresh) newItems else it.perspectives + newItems,
                            nextCursor = page.nextCursor,
                            hasNext = page.hasNext,
                            isLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    Log.e(TAG, "🔴 관점 목록 조회 실패: ${error.message}", error)
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }
}
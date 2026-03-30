package com.swyp4.team2.ui.perspective

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swyp4.team2.domain.model.PerspectiveBoard
import com.swyp4.team2.domain.model.PerspectiveDetailBoard
import com.swyp4.team2.domain.model.PerspectiveStance
import com.swyp4.team2.domain.repository.PerspectiveRepository
import com.swyp4.team2.domain.repository.VoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "PerspectiveFlow"

data class PerspectiveUiModel(
    val commentId: String,
    val profileImageUrl: String,
    val nickname: String,
    val stance: String,
    val content: String,
    val timeAgo: String,
    val replyCount: Int,
    val likeCount: Int,
    val isLiked: Boolean,
    val isMine: Boolean
)

data class PerspectiveUiState(
    val battleId: String = "",
    val proRatio: Float = 0.5f,
    val conRatio: Float = 0.5f,
    val perspectives: List<PerspectiveUiModel> = emptyList(),
    val myPerspective: PerspectiveDetailBoard? = null,
    val nextCursor: String? = null,
    val hasNext: Boolean = true,
    val isLoading: Boolean = false,
    val sort: String = "latest",
    val opinionChanged: Boolean = false
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
            perspectives = emptyList(),
            hasNext = true
        )
    )
    val uiState: StateFlow<PerspectiveUiState> = _uiState.asStateFlow()
    private var editingPerspectiveId: Long? = null

    init {
        Log.d(TAG, "ViewModel 생성됨 - 전달받은 battleId: $receivedBattleId")
        loadPerspectives()
        loadVoteStats()
        loadMyPerspective()
        loadMyVoteHistory()
    }

    private fun loadMyVoteHistory() {
        viewModelScope.launch {
            val battleIdLong = receivedBattleId.toLongOrNull() ?: 0L
            voteRepository.getMyVoteHistory(battleIdLong)
                .onSuccess { voteHistory ->
                    Log.d(TAG, "🟢 내 투표 내역 조회 성공 - 생각 변화 여부: ${voteHistory.opinionChanged}")
                    _uiState.update { it.copy(opinionChanged = voteHistory.opinionChanged) }
                }
                .onFailure { error ->
                    Log.e(TAG, "⚪ 내 투표 내역 없음 또는 에러 (정상 처리): ${error.message}")
                    _uiState.update { it.copy(opinionChanged = false) }
                }
        }
    }

    private fun loadMyPerspective() {
        viewModelScope.launch {
            val battleIdLong = receivedBattleId.toLongOrNull() ?: 0L
            perspectiveRepository.getMyPerspective(battleIdLong)
                .onSuccess { myData ->
                    Log.d(TAG, "🟢 내 관점 있음: ${myData.content} (상태: ${myData.status})")
                    _uiState.update { it.copy(myPerspective = myData) }
                }
                .onFailure {
                    Log.d(TAG, "⚪ 내 관점 없음 (정상)")
                    _uiState.update { it.copy(myPerspective = null) }
                }
        }
    }

    fun updateSort(newSort: String) {
        if (_uiState.value.sort == newSort) return
        // 정렬이 바뀌면 기존 리스트를 비우고 다시 처음부터 불러옵니다.
        _uiState.update {
            it.copy(sort = newSort, nextCursor = null, hasNext = true, perspectives = emptyList())
        }
        loadPerspectives(isRefresh = true)
    }

    fun setEditMode(perspectiveId: Long?) {
        editingPerspectiveId = perspectiveId
    }

    fun submitPerspective(content: String, onSuccess: () -> Unit) {
        if (content.isBlank()) return

        viewModelScope.launch {
            val battleIdLong = receivedBattleId.toLongOrNull() ?: 0L
            val editId = editingPerspectiveId

            if (editId != null) {
                Log.d(TAG, "관점 수정 요청 - id: $editId")
                perspectiveRepository.updatePerspective(editId, content)
                    .onSuccess {
                        Log.d(TAG, "🟢 관점 수정 성공")
                        editingPerspectiveId = null
                        onSuccess()
                        loadPerspectives(isRefresh = true)
                    }
                    .onFailure { Log.e(TAG, "🔴 관점 수정 실패", it) }
            } else {
                Log.d(TAG, "새 관점 작성 요청")
                perspectiveRepository.createPerspective(battleIdLong, content)
                    .onSuccess {
                        Log.d(TAG, "🟢 관점 작성 성공")
                        onSuccess()
                        loadMyPerspective() // 🌟 PENDING 상태일 새 글을 가져오기 위해 호출
                        loadPerspectives(isRefresh = true)
                    }
                    .onFailure { Log.e(TAG, "🔴 관점 작성 실패", it) }
            }
        }
    }

    fun deletePerspective(perspectiveId: Long) {
        viewModelScope.launch {
            Log.d(TAG, "관점 삭제 요청 - id: $perspectiveId")
            perspectiveRepository.deletePerspective(perspectiveId)
                .onSuccess {
                    Log.d(TAG, "🟢 관점 삭제 성공")
                    loadMyPerspective() // 🌟 삭제했으니 내 관점 상태도 null로 갱신!
                    loadPerspectives(isRefresh = true)
                }
                .onFailure { Log.e(TAG, "🔴 관점 삭제 실패", it) }
        }
    }

    fun retryModeration(perspectiveId: Long) {
        viewModelScope.launch {
            Log.d(TAG, "검수 재시도 요청 - id: $perspectiveId")
            perspectiveRepository.retryModeration(perspectiveId)
                .onSuccess {
                    Log.d(TAG, "🟢 검수 재시도 성공")
                    loadMyPerspective()
                }
                .onFailure { Log.e(TAG, "🔴 검수 재시도 실패", it) }
        }
    }

    private fun loadVoteStats() {
        viewModelScope.launch {
            Log.d(TAG, "투표 통계(비율) 호출 시작 - battleId: ${_uiState.value.battleId}")
            voteRepository.getVoteStats(_uiState.value.battleId.toLong())
                .onSuccess { statsBoard ->
                    val pro = statsBoard.options.find { it.label == "A" }?.ratio ?: 0.5f
                    val con = statsBoard.options.find { it.label == "B" }?.ratio ?: 0.5f

                    Log.d(TAG, "🟢 투표 통계 조회 성공 - A비율: $pro, B비율: $con")
                    _uiState.update { it.copy(proRatio = pro, conRatio = con) }
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

            Log.d(TAG, "관점(댓글) 목록 호출 시작 - battleId: $battleIdLong, sort: ${state.sort}, cursor: $cursor")

            perspectiveRepository.getPerspectives(
                battleId = battleIdLong,
                cursor = cursor,
                size = 10,
                sort = state.sort
            ).onSuccess { page ->
                Log.d(TAG, "🟢 관점 목록 조회 성공 - 가져온 개수: ${page.items.size}, hasNext: ${page.hasNext}")

                val newItems = page.items.map { it.toUiModel() }

                _uiState.update {
                    it.copy(
                        perspectives = if (isRefresh) newItems else it.perspectives + newItems,
                        nextCursor = page.nextCursor,
                        hasNext = page.hasNext,
                        isLoading = false
                    )
                }
            }.onFailure { error ->
                Log.e(TAG, "🔴 관점 목록 조회 실패: ${error.message}", error)
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun toggleLike(perspectiveId: Long, isCurrentlyLiked: Boolean) {
        viewModelScope.launch {
            val result = if (isCurrentlyLiked) {
                Log.d(TAG, "관점 좋아요 취소 요청 - id: $perspectiveId")
                perspectiveRepository.unlikePerspective(perspectiveId)
            } else {
                Log.d(TAG, "관점 좋아요 등록 요청 - id: $perspectiveId")
                perspectiveRepository.likePerspective(perspectiveId)
            }

            result.onSuccess { toggleData ->
                Log.d(TAG, "🟢 좋아요 변경 성공 (현재 좋아요 수: ${toggleData.likeCount})")

                _uiState.update { state ->
                    state.copy(
                        perspectives = state.perspectives.map { item ->
                            if (item.commentId == perspectiveId.toString()) {
                                item.copy(likeCount = toggleData.likeCount, isLiked = toggleData.isLiked)
                            } else item
                        }
                    )
                }
            }.onFailure { Log.e(TAG, "🔴 좋아요 변경 실패", it) }
        }
    }

    fun refreshAllData() {
        loadMyVoteHistory()
        loadVoteStats()
        loadMyPerspective()
        loadPerspectives(isRefresh = true)
    }
}

//  Domain -> UI
private fun PerspectiveBoard.toUiModel(): PerspectiveUiModel {
    return PerspectiveUiModel(
        commentId = this.commentId,
        profileImageUrl = this.characterImageUrl,
        nickname = this.nickname,
        stance = this.stance,
        content = this.content,
        timeAgo = this.createdAt,
        replyCount = this.replyCount,
        likeCount = this.likeCount,
        isLiked = this.isLiked,
        isMine = this.isMine
    )
}
package com.picke.app.ui.perspective

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.app.domain.model.PerspectiveBoard
import com.picke.app.domain.model.PerspectiveDetailBoard
import com.picke.app.domain.model.PollQuizVoteBoard
import com.picke.app.domain.repository.PerspectiveRepository
import com.picke.app.domain.repository.VoteRepository
import com.picke.app.domain.repository.VoteStreamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class PerspectiveUiEvent {
    data class ShowToast(val message: String) : PerspectiveUiEvent()
}

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
    val opinionChanged: Boolean = false,
    val editingPerspectiveId: Long? = null
)

@HiltViewModel
class PerspectiveViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val perspectiveRepository: PerspectiveRepository,
    private val voteRepository: VoteRepository,
    private val voteStreamRepository: VoteStreamRepository
): ViewModel() {

    companion object {
        private const val TAG = "PerspectiveVM_Picke"
    }

    private val receivedBattleId: String = checkNotNull(savedStateHandle["battleId"])
    private val _uiState = MutableStateFlow(
        PerspectiveUiState(
            battleId = receivedBattleId,
            perspectives = emptyList(),
            hasNext = true
        )
    )
    val uiState: StateFlow<PerspectiveUiState> = _uiState.asStateFlow()

    private val _realTimeStats = MutableStateFlow<PollQuizVoteBoard?>(null)
    val realTimeStats: StateFlow<PollQuizVoteBoard?> = _realTimeStats.asStateFlow()

    private val _uiEvent = MutableSharedFlow<PerspectiveUiEvent>()
    val uiEvent: SharedFlow<PerspectiveUiEvent> = _uiEvent.asSharedFlow()

    // 뷰모델 생성 시 초기 데이터 로드
    init {
        Log.d(TAG, "[FLOW] ViewModel 생성됨 - 배틀 ID: $receivedBattleId")
        loadPerspectives()
        loadVoteStats()
        loadMyPerspective()
        loadMyVoteHistory()
    }

    // 내 데이터 조회 (투표 내역, 내 관점)
    private fun loadMyVoteHistory() {
        viewModelScope.launch {
            val battleIdLong = receivedBattleId.toLongOrNull() ?: 0L
            Log.d(TAG, "[FLOW] 내 투표 내역 조회 시도")
            voteRepository.getMyVoteHistory(battleIdLong)
                .onSuccess { voteHistory ->
                    Log.i(TAG, "[STATE] 내 투표 내역 조회 성공 - 생각 변화 여부: ${voteHistory.opinionChanged}")
                    _uiState.update { it.copy(opinionChanged = voteHistory.opinionChanged) }
                }
                .onFailure { error ->
                    Log.w(TAG, "[FLOW] 내 투표 내역 없음 (정상 처리): ${error.message}")
                    _uiState.update { it.copy(opinionChanged = false) }
                }
        }
    }

    private fun loadMyPerspective() {
        viewModelScope.launch {
            val battleIdLong = receivedBattleId.toLongOrNull() ?: 0L
            Log.d(TAG, "[FLOW] 내 관점 데이터 조회 시도")
            perspectiveRepository.getMyPerspective(battleIdLong)
                .onSuccess { myData ->
                    Log.i(TAG, "[STATE] 내 관점 존재함 - 상태: ${myData.status}, 입장: ${myData.optionLabel}")
                    _uiState.update { it.copy(myPerspective = myData) }
                }
                .onFailure {
                    Log.i(TAG, "[STATE] 내 관점 없음 (작성 전)")
                    _uiState.update { it.copy(myPerspective = null) }
                }
        }
    }

    // 관점 목록 (페이징) & 정렬 로직
    fun updateSort(newSort: String) {
        if (_uiState.value.sort == newSort) return
        Log.d(TAG, "[FLOW] 정렬 기준 변경: $newSort -> 목록 초기화 후 새로고침")
        _uiState.update {
            it.copy(sort = newSort, nextCursor = null, hasNext = true, perspectives = emptyList())
        }
        loadPerspectives(isRefresh = true)
    }

    fun loadPerspectives(isRefresh: Boolean = false) {
        val state = _uiState.value
        if (!isRefresh && (state.isLoading || !state.hasNext)) return

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val cursor = if (isRefresh) null else state.nextCursor
            val battleIdLong = receivedBattleId.toLongOrNull() ?: 0L

            Log.d(TAG, "[FLOW] 관점 목록 조회 시도 - cursor: $cursor, sort: ${state.sort}")

            perspectiveRepository.getPerspectives(
                battleId = battleIdLong,
                cursor = cursor,
                size = 10,
                sort = state.sort
            ).onSuccess { page ->
                val newItems = page.items.map { it.toUiModel() }
                val actualHasNext = page.hasNext && page.nextCursor != null && newItems.isNotEmpty()

                Log.i(TAG, "[STATE] 관점 목록 조회 성공 - 가져온 개수: ${newItems.size}")

                _uiState.update { currentState ->
                    val mergedList = if (isRefresh) newItems else (currentState.perspectives + newItems).distinctBy { it.commentId }
                    currentState.copy(
                        perspectives = mergedList,
                        nextCursor = page.nextCursor,
                        hasNext = actualHasNext,
                        isLoading = false
                    )
                }
            }.onFailure { error ->
                Log.e(TAG, "[FLOW] 관점 목록 조회 실패: ${error.message}")
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun loadVoteStats() {
        viewModelScope.launch {
            Log.d(TAG, "[FLOW] 투표 통계(비율) 단건 조회 시도")
            voteRepository.getVoteStats(receivedBattleId.toLong())
                .onSuccess { statsBoard ->
                    val pro = statsBoard.options.find { it.label == "A" }?.ratio ?: 0.5f
                    val con = statsBoard.options.find { it.label == "B" }?.ratio ?: 0.5f

                    Log.i(TAG, "[STATE] 투표 통계 조회 성공 - A: $pro, B: $con")
                    _uiState.update { it.copy(proRatio = pro, conRatio = con) }
                }
                .onFailure { error ->
                    Log.w(TAG, "[FLOW] 투표 통계 조회 실패: ${error.message}")
                }
        }
    }

    // 관점 작성 / 수정 / 삭제 (핵심 낙관적 업데이트 부분)
    fun setEditMode(perspectiveId: Long?) {
        Log.d(TAG, "[STATE] 수정 모드 변경 - 대상 ID: $perspectiveId")
        _uiState.update { it.copy(editingPerspectiveId = perspectiveId) }
    }

    fun submitPerspective(content: String, onSuccess: () -> Unit) {
        if (content.isBlank()) return

        val battleIdLong = receivedBattleId.toLongOrNull() ?: 0L
        val editId = _uiState.value.editingPerspectiveId
        val isEditMode = editId != null

        Log.d(TAG, "[FLOW] 관점 ${if (isEditMode) "수정" else "작성"} 로직 시작")

        // 1. 낙관적 업데이트
        _uiState.update { state ->
            val tempLabel = state.myPerspective?.optionLabel ?: ""

            val updatedPerspective = (state.myPerspective ?: PerspectiveDetailBoard(
                perspectiveId = 0L, // 임시 ID
                content = content,
                characterImageUrl = "",
                nickname = "나",
                optionLabel = tempLabel, // 하드코딩 탈피!
                status = "PENDING",
                createdAt = "방금 전",
                likeCount = 0,
                isLiked = false,
                isMine = true,
                commentCount = 0,
                userTag = "",
            )).copy(
                status = "PENDING",
                content = content,
                optionLabel = tempLabel
            )

            state.copy(editingPerspectiveId = null, myPerspective = updatedPerspective)
        }

        onSuccess() // 입력창 닫기 및 키보드 내림

        // 2. 백엔드 통신
        viewModelScope.launch {
            if (isEditMode) {
                Log.d(TAG, "[API_REQ] 관점 수정 요청 전송")
                perspectiveRepository.updatePerspective(editId!!, content)
                    .onSuccess {
                        Log.i(TAG, "[STATE] 관점 수정 완료 -> 서버 데이터 동기화")
                        loadMyPerspective()
                        loadPerspectives(isRefresh = true)
                    }
                    .onFailure { error ->
                        Log.e(TAG, "[FLOW] 관점 수정 실패 -> UI 롤백. 원인: ${error.message}")
                        loadMyPerspective()
                    }
            } else {
                Log.d(TAG, "[API_REQ] 신규 관점 작성 요청 전송")
                perspectiveRepository.createPerspective(battleIdLong, content)
                    .onSuccess {
                        Log.i(TAG, "[STATE] 관점 작성 완료 -> 진짜 ID 확보를 위해 서버 동기화")
                        loadMyPerspective()
                        loadPerspectives(isRefresh = true)
                    }
                    .onFailure { error ->
                        Log.e(TAG, "[FLOW] 관점 작성 실패 -> UI 롤백. 원인: ${error.message}")
                        loadMyPerspective()
                    }
            }
        }
    }

    fun deletePerspective(perspectiveId: Long) {
        Log.d(TAG, "[FLOW] 관점 삭제 로직 시작 - 대상 ID: $perspectiveId")

        // 낙관적 업데이트 (UI에서 즉시 삭제)
        _uiState.update { state ->
            state.copy(
                perspectives = state.perspectives.filter { it.commentId != perspectiveId.toString() },
                myPerspective = if (state.myPerspective?.perspectiveId == perspectiveId) null else state.myPerspective
            )
        }

        viewModelScope.launch {
            Log.d(TAG, "[API_REQ] 관점 삭제 요청 전송")
            perspectiveRepository.deletePerspective(perspectiveId)
                .onSuccess {
                    Log.i(TAG, "[STATE] 관점 삭제 완료")
                }
                .onFailure { error ->
                    Log.e(TAG, "[FLOW] 관점 삭제 실패 -> UI 롤백. 원인: ${error.message}")
                    loadMyPerspective()
                    loadPerspectives(isRefresh = true)
                }
        }
    }

    // 상호작용 (좋아요, 신고, 재검수)
    fun toggleLike(perspectiveId: Long, isCurrentlyLiked: Boolean) {
        viewModelScope.launch {
            val action = if (isCurrentlyLiked) "취소" else "등록"
            Log.d(TAG, "[API_REQ] 관점 좋아요 $action 요청 - ID: $perspectiveId")

            val result = if (isCurrentlyLiked) {
                perspectiveRepository.unlikePerspective(perspectiveId)
            } else {
                perspectiveRepository.likePerspective(perspectiveId)
            }

            result.onSuccess { toggleData ->
                Log.i(TAG, "[STATE] 관점 좋아요 $action 완료 - 바뀐 좋아요 수: ${toggleData.likeCount}")
                _uiState.update { state ->
                    state.copy(
                        perspectives = state.perspectives.map { item ->
                            if (item.commentId == perspectiveId.toString()) {
                                item.copy(likeCount = toggleData.likeCount, isLiked = toggleData.isLiked)
                            } else item
                        }
                    )
                }
            }.onFailure { error ->
                Log.e(TAG, "[FLOW] 관점 좋아요 $action 실패: ${error.message}")
            }
        }
    }

    fun reportPerspective(perspectiveId: Long) {
        viewModelScope.launch {
            Log.d(TAG, "[API_REQ] 관점 신고 요청 - ID: $perspectiveId")
            perspectiveRepository.reportPerspective(perspectiveId)
                .onSuccess {
                    Log.i(TAG, "[NAV] 신고 접수 완료 토스트 노출")
                    _uiEvent.emit(PerspectiveUiEvent.ShowToast("신고가 정상 접수되었습니다."))
                }
                .onFailure { error ->
                    if (error.message == "ALREADY_REPORTED") {
                        Log.i(TAG, "[NAV] 기신고 토스트 노출")
                        _uiEvent.emit(PerspectiveUiEvent.ShowToast("이미 신고한 사용자입니다."))
                    } else {
                        Log.e(TAG, "[FLOW] 관점 신고 실패: ${error.message}")
                    }
                }
        }
    }

    fun retryModeration(perspectiveId: Long) {
        viewModelScope.launch {
            Log.d(TAG, "[API_REQ] 검수 재시도 요청 - ID: $perspectiveId")
            perspectiveRepository.retryModeration(perspectiveId)
                .onSuccess {
                    Log.i(TAG, "[STATE] 검수 재시도 성공 -> 서버 동기화")
                    loadMyPerspective()
                }
                .onFailure { Log.e(TAG, "[FLOW] 검수 재시도 실패: ${it.message}") }
        }
    }

    fun refreshAllData() {
        Log.d(TAG, "[FLOW] 당겨서 새로고침: 모든 데이터 리로드")
        loadMyVoteHistory()
        loadVoteStats()
        loadMyPerspective()
        loadPerspectives(isRefresh = true)
    }
}

// 도메인 모델 -> UI 모델 매핑 확장 함수
private fun PerspectiveBoard.toUiModel(): PerspectiveUiModel {
    val displayStance = when (this.stance.uppercase()) {
        "A", "AGREE", "찬성" -> "A"
        "B", "DISAGREE", "반대" -> "B"
        else -> this.stance
    }

    return PerspectiveUiModel(
        commentId = this.commentId,
        profileImageUrl = this.characterImageUrl,
        nickname = this.nickname,
        stance = displayStance,
        content = this.content,
        timeAgo = this.createdAt.take(10),
        replyCount = this.replyCount,
        likeCount = this.likeCount,
        isLiked = this.isLiked,
        isMine = this.isMine
    )
}
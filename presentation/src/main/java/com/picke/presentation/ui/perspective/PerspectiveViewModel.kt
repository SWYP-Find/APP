package com.picke.presentation.ui.perspective

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.feature.perspective.model.PerspectiveBoard
import com.picke.domain.feature.perspective.model.PerspectiveDetailBoard
import com.picke.domain.feature.pollquiz.model.PollQuizVoteBoard
import com.picke.domain.feature.vote.model.VoteStatsOptionBoard
import com.picke.domain.feature.perspective.usecase.PerspectiveUseCases
import com.picke.domain.feature.perspective.usecase.ReportPerspectiveResult
import com.picke.domain.feature.vote.usecase.VoteUseCases
import com.picke.presentation.util.toRelativeTimeText
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
    val optionTitle: String,
    val optionId: Long,
    val content: String,
    val timeAgo: String,
    val replyCount: Int,
    val likeCount: Int,
    val isLiked: Boolean,
    val isMine: Boolean
)

data class PerspectiveUiState(
    val battleId: String = "",
    val voteOptions: List<VoteStatsOptionBoard> = emptyList(),
    val perspectives: List<PerspectiveUiModel> = emptyList(),
    val myPerspective: PerspectiveDetailBoard? = null,
    val nextCursor: String? = null,
    val hasNext: Boolean = true,
    val isLoading: Boolean = false,
    val sort: String = "popular",
    val selectedOptionId: Long? = null,
    val opinionChanged: Boolean = false,
    val editingPerspectiveId: Long? = null,
    val battleTitle: String = ""
)

@HiltViewModel
class PerspectiveViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val perspectiveUseCases: PerspectiveUseCases,
    private val voteUseCases: VoteUseCases
) : ViewModel() {

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
            voteUseCases.getMyVoteHistoryUseCase(battleIdLong)
                .onSuccess { voteHistory ->
                    Log.i(TAG, "[STATE] 내 투표 내역 조회 성공 - 생각 변화 여부: ${voteHistory.opinionChanged}")
                    _uiState.update {
                        it.copy(
                            opinionChanged = voteHistory.opinionChanged,
                            battleTitle = voteHistory.battleTitle
                        )
                    }
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
            perspectiveUseCases.getMyPerspectiveUseCase(battleIdLong)
                .onSuccess { myData ->
                    Log.i(TAG, "[STATE] 내 관점 존재함 - 상태: ${myData.status}, 입장: ${myData.optionTitle}")
                    _uiState.update { it.copy(myPerspective = myData) }
                }
                .onFailure {
                    Log.i(TAG, "[STATE] 내 관점 없음 (작성 전)")
                    _uiState.update { it.copy(myPerspective = null) }
                }
        }
    }

    // 관점 목록 (페이징) & 정렬 / 옵션 필터 로직
    fun updateSort(newSort: String) {
        if (_uiState.value.sort == newSort) return
        Log.d(TAG, "[FLOW] 정렬 기준 변경: $newSort -> 목록 초기화 후 새로고침")
        _uiState.update {
            it.copy(sort = newSort, nextCursor = null, hasNext = true, perspectives = emptyList())
        }
        loadPerspectives(isRefresh = true)
    }

    fun selectOption(optionId: Long?) {
        if (_uiState.value.selectedOptionId == optionId) return
        Log.d(TAG, "[FLOW] 옵션 탭 변경: $optionId -> 목록 초기화 후 새로고침")
        _uiState.update {
            it.copy(
                selectedOptionId = optionId,
                nextCursor = null,
                hasNext = true,
                perspectives = emptyList()
            )
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

            Log.d(
                TAG,
                "[FLOW] 관점 목록 조회 시도 - cursor: $cursor, optionId: ${state.selectedOptionId}, sort: ${state.sort}"
            )

            perspectiveUseCases.loadPerspectivesUseCase(
                battleId = battleIdLong,
                cursor = cursor,
                size = 10,
                optionId = state.selectedOptionId,
                sort = state.sort
            ).onSuccess { page ->
                val newItems = page.items.map { it.toUiModel() }
                val actualHasNext = page.hasNext && page.nextCursor != null && newItems.isNotEmpty()

                Log.i(TAG, "[STATE] 관점 목록 조회 성공 - 가져온 개수: ${newItems.size}")

                _uiState.update { currentState ->
                    val mergedList =
                        if (isRefresh) newItems else (currentState.perspectives + newItems).distinctBy { it.commentId }
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
            voteUseCases.getVoteStatsUseCase(receivedBattleId.toLong())
                .onSuccess { statsBoard ->
                    Log.i(TAG, "[STATE] 투표 통계 조회 성공 - 옵션 수: ${statsBoard.options.size}")
                    _uiState.update { it.copy(voteOptions = statsBoard.options) }
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

        _uiState.update { it.copy(editingPerspectiveId = null) }

        // 백엔드 통신
        viewModelScope.launch {
            Log.d(TAG, "[API_REQ] 관점 ${if (isEditMode) "수정" else "작성"} 요청 전송")
            perspectiveUseCases.submitPerspectiveUseCase(battleIdLong, editId, content)
                .onSuccess {
                    Log.i(TAG, "[STATE] 관점 ${if (isEditMode) "수정" else "작성"} 완료 -> 서버 데이터 동기화")
                    onSuccess() // 입력창 닫기 및 키보드 내림
                    loadMyPerspective()
                    loadPerspectives(isRefresh = true)
                }
                .onFailure { error ->
                    Log.e(
                        TAG,
                        "[FLOW] 관점 ${if (isEditMode) "수정" else "작성"} 실패 -> UI 롤백. 원인: ${error.message}"
                    )
                    loadMyPerspective()
                }
        }
    }

    fun deletePerspective(perspectiveId: Long) {
        Log.d(
            TAG,
            "[FLOW] 관점 삭제 로직 시작 - perspectiveId: $perspectiveId, battleId: $receivedBattleId"
        )

        // 낙관적 업데이트 (UI에서 즉시 삭제)
        _uiState.update { state ->
            state.copy(
                perspectives = state.perspectives.filter { it.commentId != perspectiveId.toString() },
                myPerspective = if (state.myPerspective?.perspectiveId == perspectiveId) null else state.myPerspective
            )
        }

        viewModelScope.launch {
            Log.d(
                TAG,
                "[API_REQ] 관점 삭제 요청 전송 - perspectiveId: $perspectiveId, battleId: $receivedBattleId"
            )
            perspectiveUseCases.deletePerspectiveUseCase(perspectiveId)
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

            perspectiveUseCases.togglePerspectiveLikeUseCase(perspectiveId, isCurrentlyLiked)
                .onSuccess { toggleData ->
                    Log.i(TAG, "[STATE] 관점 좋아요 $action 완료 - 바뀐 좋아요 수: ${toggleData.likeCount}")
                    _uiState.update { state ->
                        state.copy(
                            perspectives = state.perspectives.map { item ->
                                if (item.commentId == perspectiveId.toString()) {
                                    item.copy(
                                        likeCount = toggleData.likeCount,
                                        isLiked = toggleData.isLiked
                                    )
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
            perspectiveUseCases.reportPerspectiveUseCase(perspectiveId)
                .onSuccess { result ->
                    when (result) {
                        is ReportPerspectiveResult.Reported -> {
                            Log.i(TAG, "[NAV] 신고 접수 완료 토스트 노출")
                            _uiEvent.emit(PerspectiveUiEvent.ShowToast("신고가 정상 접수되었습니다."))
                        }

                        is ReportPerspectiveResult.AlreadyReported -> {
                            Log.i(TAG, "[NAV] 기신고 토스트 노출")
                            _uiEvent.emit(PerspectiveUiEvent.ShowToast("이미 신고한 사용자입니다."))
                        }
                    }
                }
                .onFailure { error ->
                    Log.e(TAG, "[FLOW] 관점 신고 실패: ${error.message}")
                }
        }
    }

    fun retryModeration(perspectiveId: Long) {
        viewModelScope.launch {
            Log.d(TAG, "[API_REQ] 검수 재시도 요청 - ID: $perspectiveId")
            perspectiveUseCases.retryModerationUseCase(perspectiveId)
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
private fun PerspectiveBoard.toUiModel() = PerspectiveUiModel(
    commentId = this.commentId,
    profileImageUrl = this.characterImageUrl,
    nickname = this.nickname,
    optionTitle = this.optionTitle,
    optionId = this.optionId,
    content = this.content,
    timeAgo = this.createdAt.toRelativeTimeText(),
    replyCount = this.replyCount,
    likeCount = this.likeCount,
    isLiked = this.isLiked,
    isMine = this.isMine
)

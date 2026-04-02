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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "PerspectiveFlow"

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

    private val _uiEvent = MutableSharedFlow<PerspectiveUiEvent>()
    val uiEvent: SharedFlow<PerspectiveUiEvent> = _uiEvent.asSharedFlow()

    // 뷰모델 생성 시 초기 데이터 로드
    init {
        Log.d(TAG, "ViewModel 생성됨 - 전달받은 battleId: $receivedBattleId")
        loadPerspectives()
        loadVoteStats()
        loadMyPerspective()
        loadMyVoteHistory()
    }

    // 내 투표 내역 (생각 변화 여부 등) 조회
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

    // 내가 작성한 관점(댓글) 조회
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

    // 정렬 기준 변경 (인기순/최신순) 및 목록 초기화 후 새로고침
    fun updateSort(newSort: String) {
        if (_uiState.value.sort == newSort) return
        _uiState.update {
            it.copy(sort = newSort, nextCursor = null, hasNext = true, perspectives = emptyList())
        }
        loadPerspectives(isRefresh = true)
    }

    // 관점 수정 모드 진입/해제 (수정할 댓글의 ID를 상태에 저장)
    fun setEditMode(perspectiveId: Long?) {
        _uiState.update { it.copy(editingPerspectiveId = perspectiveId) }
    }

    // 관점 작성(생성) 또는 수정 요청 전송
    fun submitPerspective(content: String, onSuccess: () -> Unit) {
        if (content.isBlank()) return

        val battleIdLong = receivedBattleId.toLongOrNull() ?: 0L
        val editId = _uiState.value.editingPerspectiveId

        _uiState.update { state ->
            val updatedPerspective = (state.myPerspective ?: PerspectiveDetailBoard(
                perspectiveId = 0L,
                content = content,
                characterImageUrl = "",
                nickname = "나",
                optionLabel = "A",
                status = "PENDING",
                createdAt = "",
                likeCount = 0,
                isLiked = false,
                isMine = true,
                commentCount = 0,
                userTag = "",
            )).copy(
                status = "PENDING",
                content = content
            )

            state.copy(
                editingPerspectiveId = null,
                myPerspective = updatedPerspective
            )
        }

        onSuccess()

        viewModelScope.launch {
            if (editId != null) {
                perspectiveRepository.updatePerspective(editId, content)
                    .onSuccess {
                        Log.d(TAG, "🟢 관점 수정 성공")
                        loadMyPerspective()
                        loadPerspectives(isRefresh = true)
                    }
                    .onFailure { Log.e(TAG, "🔴 관점 수정 실패", it) }
            } else {
                perspectiveRepository.createPerspective(battleIdLong, content)
                    .onSuccess {
                        Log.d(TAG, "🟢 관점 작성 성공")
                        loadMyPerspective()
                        loadPerspectives(isRefresh = true)
                    }
                    .onFailure { Log.e(TAG, "🔴 관점 작성 실패", it) }
            }
        }
    }

    // 내 관점 삭제
    fun deletePerspective(perspectiveId: Long) {
        _uiState.update { state ->
            state.copy(
                perspectives = state.perspectives.filter { it.commentId != perspectiveId.toString() },
                myPerspective = if (state.myPerspective?.perspectiveId == perspectiveId) null else state.myPerspective
            )
        }

        viewModelScope.launch {
            Log.d(TAG, "관점 삭제 요청 - id: $perspectiveId")
            perspectiveRepository.deletePerspective(perspectiveId)
                .onSuccess {
                    Log.d(TAG, "🟢 관점 삭제 성공 (UI는 이미 업데이트됨!)")
                    // 성공 시 굳이 서버에 새 리스트를 당장 달라고 조르지 않습니다. (서버 동기화 시간 벌어주기)
                }
                .onFailure { error ->
                    Log.e(TAG, "🔴 관점 삭제 실패 - 원상복구 진행", error)
                    // 만약 서버 통신에 실패했다면, 지웠던 걸 다시 살려내기 위해 새로고침을 합니다.
                    loadMyPerspective()
                    loadPerspectives(isRefresh = true)
                }
        }
    }

    // 거절된 관점 재검수 요청
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

    // 배틀의 찬/반 투표 비율 통계 조회
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

    // 관점(댓글) 목록 페이징 조회 (무한 스크롤 방어 로직 포함)
    fun loadPerspectives(isRefresh: Boolean = false) {
        val state = _uiState.value
        // 수정: 새로고침(isRefresh)일 때는 isLoading이어도 무시하고 진행합니다.
        // 기존: if (state.isLoading || (!isRefresh && !state.hasNext)) return
        if (!isRefresh && (state.isLoading || !state.hasNext)) return

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
                val newItems = page.items.map { it.toUiModel() }
                val actualHasNext = page.hasNext && page.nextCursor != null && newItems.isNotEmpty()

                _uiState.update { currentState ->
                    val mergedList = if (isRefresh) {
                        newItems
                    } else {
                        (currentState.perspectives + newItems).distinctBy { it.commentId }
                    }

                    currentState.copy(
                        perspectives = mergedList,
                        nextCursor = page.nextCursor,
                        hasNext = actualHasNext,
                        isLoading = false
                    )
                }
            }.onFailure { error ->
                Log.e(TAG, "🔴 관점 목록 조회 실패: ${error.message}", error)
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    // 관점 좋아요 등록/취소 토글
    fun toggleLike(perspectiveId: Long, isCurrentlyLiked: Boolean) {
        viewModelScope.launch {
            val actionType = if (isCurrentlyLiked) "취소(Unlike)" else "등록(Like)"

            Log.d(TAG, "=========================================")
            Log.d(TAG, "🔍 [좋아요 $actionType] 버튼 클릭됨!")
            Log.d(TAG, "🔍 대상 관점 ID: $perspectiveId | 현재 프론트엔드 좋아요 상태: $isCurrentlyLiked")

            val result = if (isCurrentlyLiked) {
                perspectiveRepository.unlikePerspective(perspectiveId)
            } else {
                perspectiveRepository.likePerspective(perspectiveId)
            }

            result.onSuccess { toggleData ->
                Log.d(TAG, "🟢 [좋아요 $actionType] 서버 통신 성공!")
                Log.d(TAG, "🟢 서버에서 준 새 데이터 -> 좋아요 수: ${toggleData.likeCount}, 내 좋아요 상태: ${toggleData.isLiked}")

                _uiState.update { state ->
                    val updatedPerspectives = state.perspectives.map { item ->
                        if (item.commentId == perspectiveId.toString()) {
                            // UI에 값이 잘 들어가는지 확인하는 로그
                            Log.d(TAG, "🟢 UI 리스트 업데이트 완료! 기존 좋아요 수[${item.likeCount}] -> 변경된 좋아요 수[${toggleData.likeCount}]")
                            item.copy(
                                likeCount = toggleData.likeCount,
                                isLiked = toggleData.isLiked
                            )
                        } else item
                    }
                    state.copy(perspectives = updatedPerspectives)
                }
                Log.d(TAG, "=========================================")
            }.onFailure { error ->
                Log.e(TAG, "🔴 [좋아요 $actionType] 서버 통신 실패 - 에러: ${error.message}", error)
                Log.d(TAG, "=========================================")
            }
        }
    }

    // 타인 관점 신고
    fun reportPerspective(perspectiveId: Long) {
        viewModelScope.launch {
            Log.d(TAG, "관점 신고 요청 - id: $perspectiveId")
            perspectiveRepository.reportPerspective(perspectiveId)
                .onSuccess {
                    _uiEvent.emit(PerspectiveUiEvent.ShowToast("신고가 정상 접수되었습니다."))
                }
                .onFailure { error ->
                    if (error.message == "ALREADY_REPORTED") {
                        _uiEvent.emit(PerspectiveUiEvent.ShowToast("이미 신고한 사용자입니다."))
                    } else {
                        Log.e(TAG, "🔴 관점 신고 실패", error)
                    }
                }
        }
    }

    // 당겨서 새로고침 시 모든 데이터 갱신
    fun refreshAllData() {
        loadMyVoteHistory()
        loadVoteStats()
        loadMyPerspective()
        loadPerspectives(isRefresh = true)
    }
}


// Domain Model -> UI Model
private fun PerspectiveBoard.toUiModel(): PerspectiveUiModel {
    val displayStance = when (this.stance) {
        "A", "AGREE", "찬성" -> "찬성"
        "B", "DISAGREE", "반대" -> "반대"
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
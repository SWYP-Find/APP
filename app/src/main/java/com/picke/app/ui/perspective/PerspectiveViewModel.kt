package com.picke.app.ui.perspective

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.app.domain.model.PerspectiveBoard
import com.picke.app.domain.model.PerspectiveDetailBoard
import com.picke.app.domain.repository.PerspectiveRepository
import com.picke.app.domain.repository.VoteRepository
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
        if (content.isBlank()) {
            Log.d(TAG, "⚠️ [관점 작성] 내용이 비어있어 요청을 무시합니다.")
            return
        }

        val battleIdLong = receivedBattleId.toLongOrNull() ?: 0L
        val editId = _uiState.value.editingPerspectiveId
        val isEditMode = editId != null

        Log.d(TAG, "=========================================")
        Log.d(TAG, "📝 [관점 ${if (isEditMode) "수정" else "작성"}] 요청 시작!")
        Log.d(TAG, "   - 배틀 ID: $battleIdLong | 수정대상 ID: $editId")
        Log.d(TAG, "   - 입력한 내용: $content")

        // 1. 낙관적 업데이트 (서버 응답 전에 내 관점 영역에 '검수중(PENDING)' 상태로 먼저 띄움)
        Log.d(TAG, "   - 📱 [UI 선반영] 내 관점 카드를 '검수중(PENDING)' 상태로 즉시 변경합니다.")
        _uiState.update { state ->
            val updatedPerspective = (state.myPerspective ?: PerspectiveDetailBoard(
                perspectiveId = 0L, // 서버 통신 전 임시 ID
                content = content,
                characterImageUrl = "",
                nickname = "나",
                optionLabel = "A", // 투표 내역에 따라 다르지만 UI상 임시로 둠
                status = "PENDING",
                createdAt = "방금 전",
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

        // 입력창 비우기 및 키보드 내리기 동작 실행
        onSuccess()

        // 2. 실제 서버 API 통신
        viewModelScope.launch {
            if (isEditMode) {
                // [수정 모드]
                Log.d(TAG, "   - 📤 [Request] 서버로 관점 '수정' API 호출 시작...")
                perspectiveRepository.updatePerspective(editId!!, content)
                    .onSuccess {
                        Log.d(TAG, "   - ✅ [Response] 관점 수정 통신 성공!")
                        Log.d(TAG, "   - 🔄 최신 데이터를 위해 내 관점 및 리스트를 다시 불러옵니다.")
                        loadMyPerspective()
                        loadPerspectives(isRefresh = true)
                        Log.d(TAG, "=========================================")
                    }
                    .onFailure { error ->
                        Log.e(TAG, "   - ❌ [Error] 관점 수정 실패! 원인: ${error.message}", error)
                        Log.d(TAG, "   - 🔄 [Rollback] 수정 실패로 인해 이전 상태로 원상복구합니다.")
                        loadMyPerspective()
                        Log.d(TAG, "=========================================")
                    }
            } else {
                // [작성 모드]
                Log.d(TAG, "   - 📤 [Request] 서버로 관점 '작성' API 호출 시작...")
                perspectiveRepository.createPerspective(battleIdLong, content)
                    .onSuccess {
                        Log.d(TAG, "   - ✅ [Response] 관점 작성 통신 성공!")
                        Log.d(TAG, "   - 🔄 서버가 부여한 진짜 ID를 받기 위해 내 관점을 다시 불러옵니다.")
                        loadMyPerspective()
                        loadPerspectives(isRefresh = true)
                        Log.d(TAG, "=========================================")
                    }
                    .onFailure { error ->
                        Log.e(TAG, "   - ❌ [Error] 관점 작성 실패! 원인: ${error.message}", error)
                        Log.d(TAG, "   - 🔄 [Rollback] 작성 실패로 인해 임시로 띄운 UI 카드를 삭제합니다.")
                        loadMyPerspective()
                        Log.d(TAG, "=========================================")
                    }
            }
        }
    }

    // 내 관점 삭제
    fun deletePerspective(perspectiveId: Long) {
        Log.d(TAG, "=========================================")
        Log.d(TAG, "🗑️ [관점 삭제] 버튼 클릭됨! 대상 ID: $perspectiveId")

        // 1. 삭제 전 상태 기록
        val beforeListSize = _uiState.value.perspectives.size
        val isMyPerspectiveDeleted = _uiState.value.myPerspective?.perspectiveId == perspectiveId

        // 2. 낙관적 업데이트 (서버 통신 전 UI부터 즉시 삭제)
        _uiState.update { state ->
            state.copy(
                perspectives = state.perspectives.filter { it.commentId != perspectiveId.toString() },
                myPerspective = if (state.myPerspective?.perspectiveId == perspectiveId) null else state.myPerspective
            )
        }

        // 3. 삭제 후 상태 확인 로그
        val afterListSize = _uiState.value.perspectives.size
        Log.d(TAG, "   - 📱 [UI 선반영] 전체 목록 개수 변화: $beforeListSize 개 -> $afterListSize 개")
        if (isMyPerspectiveDeleted) {
            Log.d(TAG, "   - 📱 [UI 선반영] 최상단 '내 관점' 카드 데이터 삭제(null 처리) 완료")
        }

        // 4. 실제 서버에 API 요청
        viewModelScope.launch {
            Log.d(TAG, "   - 📤 [Request] 서버로 관점 삭제 API 호출 시작...")
            perspectiveRepository.deletePerspective(perspectiveId)
                .onSuccess {
                    Log.d(TAG, "   - ✅ [Response] 서버 통신 성공! DB에서도 완벽히 삭제되었습니다.")
                    Log.d(TAG, "=========================================")
                }
                .onFailure { error ->
                    Log.e(TAG, "   - ❌ [Error] 서버 삭제 실패! 원인(메시지): ${error.message}", error)
                    Log.d(TAG, "   - 🔄 [Rollback] 삭제 실패로 인해 데이터를 원상복구(새로고침)합니다.")
                    Log.d(TAG, "=========================================")

                    // 실패 시 지웠던 UI를 복구하기 위해 데이터를 다시 불러옵니다.
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
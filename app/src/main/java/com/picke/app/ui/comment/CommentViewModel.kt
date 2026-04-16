package com.picke.app.ui.comment

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.app.domain.model.CommentBoard
import com.picke.app.domain.repository.CommentRepository
import com.picke.app.domain.repository.PerspectiveRepository
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

sealed class CommentUiEvent {
    data class ShowToast(val message: String) : CommentUiEvent()
}

data class CommentUiModel(
    val commentId: String,
    val profileImageUrl: String,
    val nickname: String,
    val stance: String,
    val content: String,
    val timeAgo: String,
    val likeCount: Int,
    val isLiked: Boolean,
    val isMine: Boolean,
    val replyCount: Int = 0
)

data class CommentUiState(
    val targetId: String = "",
    val mainPerspective: CommentUiModel? = null,
    val comments: List<CommentUiModel> = emptyList(),
    val nextCursor: String? = null,
    val hasNext: Boolean = true,
    val isLoading: Boolean = false,
    val editingCommentId: Long? = null
)

@HiltViewModel
class CommentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val commentRepository: CommentRepository,
    private val perspectiveRepository: PerspectiveRepository,
) : ViewModel() {

    companion object {
        private const val TAG = "CommentViewModel_Picke"
    }

    private val _uiEvent = MutableSharedFlow<CommentUiEvent>()
    val uiEvent: SharedFlow<CommentUiEvent> = _uiEvent.asSharedFlow()

    private val receivedTargetId: String = checkNotNull(savedStateHandle["itemId"])
    private val _uiState = MutableStateFlow(
        CommentUiState(
            targetId = receivedTargetId,
            comments = emptyList(),
            hasNext = true
        )
    )
    val uiState: StateFlow<CommentUiState> = _uiState.asStateFlow()
    private var editingCommentId: Long? = null

    init {
        Log.d(TAG, "[FLOW] ViewModel 생성됨 - targetId: $receivedTargetId")
        loadMainPerspective()
        loadComments()
    }

    // 해당 댓글 단건 조회
    fun loadMainPerspective() {
        viewModelScope.launch {
            val targetIdLong = receivedTargetId.toLongOrNull() ?: 0L
            Log.d(TAG, "[FLOW] 메인 관점(본문) 데이터 호출 시작")

            perspectiveRepository.getPerspective(targetIdLong)
                .onSuccess { perspective ->
                    Log.i(TAG, "[STATE] 메인 관점(본문) 조회 성공")

                    val displayStance = if (perspective.optionLabel == "A" || perspective.optionLabel == "AGREE") "A" else "B"

                    _uiState.update { state ->
                        state.copy(
                            mainPerspective = CommentUiModel(
                                commentId = perspective.perspectiveId.toString(),
                                profileImageUrl = perspective.characterImageUrl,
                                nickname = perspective.nickname,
                                stance = displayStance,
                                content = perspective.content,
                                timeAgo = perspective.createdAt.take(10),
                                likeCount = perspective.likeCount,
                                isLiked = perspective.isLiked,
                                isMine = perspective.isMine,
                                replyCount = perspective.commentCount
                            )
                        )
                    }
                }
                .onFailure { Log.w(TAG, "[FLOW] 메인 관점(본문) 조회 실패: ${it.message}") }
        }
    }

    // 댓글 목록 조회
    fun loadComments(isRefresh: Boolean = false) {
        val state = _uiState.value
        if (state.isLoading || (!isRefresh && !state.hasNext)) return

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val cursor = if (isRefresh) null else state.nextCursor
            val targetIdLong = receivedTargetId.toLongOrNull() ?: 0L

            Log.d(TAG, "[FLOW] 댓글 목록 호출 시작. Cursor: $cursor")

            commentRepository.getComments(targetIdLong, cursor, size = 10)
                .onSuccess { page ->
                    Log.i(TAG, "[STATE] 댓글 목록 조회 성공 - 가져온 개수: ${page.items.size}")
                    val newItems = page.items.map { it.toUiModel() }

                    _uiState.update {
                        it.copy(
                            comments = if (isRefresh) newItems else it.comments + newItems,
                            nextCursor = page.nextCursor,
                            hasNext = page.hasNext,
                            isLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    Log.w(TAG, "[FLOW] 댓글 목록 조회 실패: ${error.message}")
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }

    // 댓글 작성 및 수정
    fun submitComment(content: String, onSuccess: () -> Unit) {
        if (content.isBlank()) return

        val targetIdLong = receivedTargetId.toLongOrNull() ?: 0L
        val editId = _uiState.value.editingCommentId

        _uiState.update { it.copy(editingCommentId = null) }

        viewModelScope.launch {
            if (editId != null) {
                Log.d(TAG, "[FLOW] 댓글 수정 요청 시작. ID: $editId")
                commentRepository.updateComment(targetIdLong, editId, content)
                    .onSuccess {
                        Log.i(TAG, "[STATE] 댓글 수정 완료")
                        onSuccess()
                        loadComments(isRefresh = true)
                    }
                    .onFailure { Log.w(TAG, "[FLOW] 댓글 수정 실패: ${it.message}") }
            } else {
                Log.d(TAG, "[FLOW] 댓글 작성 요청 시작")
                commentRepository.createComment(targetIdLong, content)
                    .onSuccess {
                        Log.i(TAG, "[STATE] 댓글 작성 완료")
                        onSuccess()
                        loadComments(isRefresh = true)
                    }
                    .onFailure { Log.w(TAG, "[FLOW] 댓글 작성 실패: ${it.message}") }
            }
        }
    }

    // 댓글 삭제
    fun deleteComment(commentId: Long) {
        // 낙관적 UI 업데이트
        Log.d(TAG, "[STATE] 삭제 전 UI 선반영 완료")
        _uiState.update { state ->
            state.copy(
                comments = state.comments.filter { it.commentId != commentId.toString() }
            )
        }

        viewModelScope.launch {
            val targetIdLong = receivedTargetId.toLongOrNull() ?: 0L
            Log.d(TAG, "[FLOW] 댓글 삭제 요청. TargetId: $targetIdLong, CommentId: $commentId")

            commentRepository.deleteComment(targetIdLong, commentId)
                .onSuccess {
                    Log.i(TAG, "[STATE] 댓글 삭제 통신 성공")
                    loadMainPerspective()
                }
                .onFailure { error ->
                    Log.w(TAG, "[FLOW] 댓글 삭제 실패 - 원상복구 진행: ${error.message}")
                    loadComments(isRefresh = true)
                }
        }
    }

    // 댓글 좋아요 토글 (등록/취소)
    fun toggleLike(commentId: Long, isCurrentlyLiked: Boolean) {
        viewModelScope.launch {
            val result = if (isCurrentlyLiked) {
                Log.d(TAG, "[FLOW] 댓글 좋아요 취소 요청. ID: $commentId")
                commentRepository.unlikeComment(commentId)
            } else {
                Log.d(TAG, "[FLOW] 댓글 좋아요 등록 요청. ID: $commentId")
                commentRepository.likeComment(commentId)
            }

            result.onSuccess { toggleData ->
                Log.i(TAG, "[STATE] 댓글 좋아요 변경 완료 (현재 수: ${toggleData.likeCount})")

                _uiState.update { state ->
                    state.copy(
                        comments = state.comments.map { item ->
                            if (item.commentId == commentId.toString()) {
                                item.copy(likeCount = toggleData.likeCount, isLiked = toggleData.isLiked)
                            } else item
                        }
                    )
                }
            }.onFailure { Log.w(TAG, "[FLOW] 댓글 좋아요 변경 실패: ${it.message}") }
        }
    }

    // 해당 댓글 좋아요 조회
    fun toggleMainPerspectiveLike() {
        val mainItem = _uiState.value.mainPerspective ?: return
        viewModelScope.launch {
            val targetIdLong = receivedTargetId.toLongOrNull() ?: 0L

            Log.d(TAG, "[FLOW] 메인 관점 좋아요 토글 요청. 현재 상태: ${mainItem.isLiked}")

            val result = if (mainItem.isLiked) {
                perspectiveRepository.unlikePerspective(targetIdLong)
            } else {
                perspectiveRepository.likePerspective(targetIdLong)
            }

            result.onSuccess { toggleData ->
                Log.i(TAG, "[STATE] 메인 관점 좋아요 변경 완료 (현재 수: ${toggleData.likeCount})")
                _uiState.update { state ->
                    state.copy(
                        mainPerspective = state.mainPerspective?.copy(
                            likeCount = toggleData.likeCount,
                            isLiked = toggleData.isLiked
                        )
                    )
                }
            }.onFailure { Log.w(TAG, "[FLOW] 메인 관점 좋아요 토글 실패: ${it.message}") }
        }
    }

    // 댓글 수정 모드 진입/해제
    fun setEditMode(commentId: Long?) {
        Log.d(TAG, "[STATE] 수정 모드 변경. Comment ID: $commentId")
        _uiState.update { it.copy(editingCommentId = commentId) }
    }

    // 타인 댓글 신고
    fun reportComment(commentId: Long) {
        viewModelScope.launch {
            val targetIdLong = receivedTargetId.toLongOrNull() ?: 0L
            Log.d(TAG, "[FLOW] 댓글 신고 요청. ID: $commentId")

            commentRepository.reportComment(targetIdLong, commentId)
                .onSuccess {
                    Log.i(TAG, "[NAV] 신고 접수 완료 토스트 노출")
                    _uiEvent.emit(CommentUiEvent.ShowToast("신고가 정상 접수되었습니다."))
                }
                .onFailure { error ->
                    if (error.message == "ALREADY_REPORTED") {
                        Log.i(TAG, "[NAV] 이미 신고한 사용자 토스트 노출")
                        _uiEvent.emit(CommentUiEvent.ShowToast("이미 신고한 사용자입니다."))
                    } else {
                        Log.w(TAG, "[FLOW] 댓글 신고 실패: ${error.message}")
                    }
                }
        }
    }

    // 새로고침 시 모든 데이터 갱신
    fun refreshAllData() {
        Log.d(TAG, "[FLOW] 당겨서 새로고침: 모든 데이터 리로드")
        loadMainPerspective()
        loadComments(isRefresh = true)
    }
}

private fun CommentBoard.toUiModel(): CommentUiModel {
    val displayStance = when (this.stance){
        "A", "AGREE", "찬성" -> "A"
        "B", "DISAGREE", "반대" -> "B"
        else -> this.stance
    }

    return CommentUiModel(
        commentId = this.commentId.toString(),
        profileImageUrl = this.user.characterImageUrl,
        nickname = this.user.nickname,
        stance = displayStance,
        content = this.content,
        timeAgo = this.createdAt.take(10),
        likeCount = this.likeCount,
        isLiked = this.isLiked,
        isMine = this.isMine
    )
}
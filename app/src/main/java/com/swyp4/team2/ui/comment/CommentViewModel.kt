package com.swyp4.team2.ui.comment

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swyp4.team2.domain.model.CommentBoard
import com.swyp4.team2.domain.repository.CommentRepository
import com.swyp4.team2.domain.repository.PerspectiveRepository
import com.swyp4.team2.ui.perspective.PerspectiveViewModel
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

private const val TAG = "CommentFlow"

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
        Log.d(TAG, "ViewModel 생성됨 - targetId: $receivedTargetId")
        loadMainPerspective()
        loadComments()
    }

    // 해당 댓글 단건 조회
    fun loadMainPerspective() {
        viewModelScope.launch {
            val targetIdLong = receivedTargetId.toLongOrNull() ?: 0L
            perspectiveRepository.getPerspective(targetIdLong)
                .onSuccess { perspective ->
                    Log.d(TAG, "🟢 메인 관점(본문) 조회 성공")

                    val displayStance = if (perspective.optionLabel == "A" || perspective.optionLabel == "AGREE") "찬성" else "반대"

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
                .onFailure { Log.e(TAG, "🔴 메인 관점(본문) 조회 실패", it) }
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

            commentRepository.getComments(targetIdLong, cursor, size = 10)
                .onSuccess { page ->
                    Log.d(TAG, "🟢 댓글 목록 조회 성공 - 가져온 개수: ${page.items.size}")
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
                    Log.e(TAG, "🔴 댓글 목록 조회 실패: ${error.message}", error)
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
                commentRepository.updateComment(targetIdLong, editId, content)
                    .onSuccess {
                        Log.d(TAG, "🟢 댓글 수정 성공")
                        onSuccess()
                        loadComments(isRefresh = true)
                    }
                    .onFailure { Log.e(TAG, "🔴 댓글 수정 실패", it) }
            } else {
                commentRepository.createComment(targetIdLong, content)
                    .onSuccess {
                        Log.d(TAG, "🟢 댓글 작성 성공")
                        onSuccess()
                        loadComments(isRefresh = true)
                    }
                    .onFailure { Log.e(TAG, "🔴 댓글 작성 실패", it) }
            }
        }
    }

    // 댓글 삭제
    fun deleteComment(commentId: Long) {
        _uiState.update { state ->
            state.copy(
                comments = state.comments.filter { it.commentId != commentId.toString() }
            )
        }

        viewModelScope.launch {
            val targetIdLong = receivedTargetId.toLongOrNull() ?: 0L
            Log.d(TAG, "댓글 삭제 요청 - targetId: $targetIdLong, commentId: $commentId")

            commentRepository.deleteComment(targetIdLong, commentId)
                .onSuccess {
                    Log.d(TAG, "🟢 댓글 삭제 성공 (UI는 이미 업데이트됨!)")

                    loadMainPerspective()
                }
                .onFailure { error ->
                    Log.e(TAG, "🔴 댓글 삭제 실패 - 원상복구 진행", error)
                    loadComments(isRefresh = true)
                }
        }
    }

    // 댓글 좋아요 토글 (등록/취소)
    fun toggleLike(commentId: Long, isCurrentlyLiked: Boolean) {
        viewModelScope.launch {
            val result = if (isCurrentlyLiked) {
                Log.d(TAG, "댓글 좋아요 취소 요청 - id: $commentId")
                commentRepository.unlikeComment(commentId)
            } else {
                Log.d(TAG, "댓글 좋아요 등록 요청 - id: $commentId")
                commentRepository.likeComment(commentId)
            }

            result.onSuccess { toggleData ->
                Log.d(TAG, "🟢 댓글 좋아요 변경 성공 (현재 좋아요 수: ${toggleData.likeCount})")

                // 성공하면 상태 업데이트 (해당 코멘트만 쏙 바꿔 끼우기)
                _uiState.update { state ->
                    state.copy(
                        comments = state.comments.map { item ->
                            if (item.commentId == commentId.toString()) {
                                item.copy(likeCount = toggleData.likeCount, isLiked = toggleData.isLiked)
                            } else item
                        }
                    )
                }
            }.onFailure { Log.e(TAG, "🔴 댓글 좋아요 변경 실패", it) }
        }
    }

    // 해당 댓글 좋아요 조회
    fun toggleMainPerspectiveLike() {
        val mainItem = _uiState.value.mainPerspective ?: return
        viewModelScope.launch {
            val targetIdLong = receivedTargetId.toLongOrNull() ?: 0L
            val result = if (mainItem.isLiked) {
                perspectiveRepository.unlikePerspective(targetIdLong)
            } else {
                perspectiveRepository.likePerspective(targetIdLong)
            }

            result.onSuccess { toggleData ->
                _uiState.update { state ->
                    state.copy(
                        mainPerspective = state.mainPerspective?.copy(
                            likeCount = toggleData.likeCount,
                            isLiked = toggleData.isLiked
                        )
                    )
                }
            }
        }
    }

    // 댓글 수정 모드 진입/해제
    fun setEditMode(commentId: Long?) {
        _uiState.update { it.copy(editingCommentId = commentId) }
    }

    // 타인 댓글 신고
    fun reportComment(commentId: Long) {
        viewModelScope.launch {
            val targetIdLong = receivedTargetId.toLongOrNull() ?: 0L
            commentRepository.reportComment(targetIdLong, commentId)
                .onSuccess {
                    _uiEvent.emit(CommentUiEvent.ShowToast("신고가 정상 접수되었습니다."))
                }
                .onFailure { error ->
                    // 🌟 409 에러 키워드 확인
                    if (error.message == "ALREADY_REPORTED") {
                        _uiEvent.emit(CommentUiEvent.ShowToast("이미 신고한 사용자입니다."))
                    } else {
                        Log.e(TAG, "🔴 댓글 신고 실패: ${error.message}")
                    }
                }
         }
    }

    // 새로고침 시 모든 데이터 갱신
    fun refreshAllData() {
        loadMainPerspective()
        loadComments(isRefresh = true)
    }
}

private fun CommentBoard.toUiModel(): CommentUiModel {
    return CommentUiModel(
        commentId = this.commentId.toString(),
        profileImageUrl = this.user.characterImageUrl,
        nickname = this.user.nickname,
        stance = if (this.stance == "A" || this.stance == "AGREE") "찬성" else "반대",
        content = this.content,
        timeAgo = this.createdAt.take(10),
        likeCount = this.likeCount,
        isLiked = this.isLiked,
        isMine = this.isMine
    )
}
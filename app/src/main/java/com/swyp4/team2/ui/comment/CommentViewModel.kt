package com.swyp4.team2.ui.comment

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swyp4.team2.domain.model.CommentBoard
import com.swyp4.team2.domain.repository.CommentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "CommentFlow"

data class CommentUiModel(
    val commentId: String,
    val profileImageUrl: String,
    val nickname: String,
    val stance: String,
    val content: String,
    val timeAgo: String,
    val likeCount: Int,
    val isLiked: Boolean,
    val isMine: Boolean
)

data class CommentUiState(
    val targetId: String = "",
    val comments: List<CommentUiModel> = emptyList(),
    val nextCursor: String? = null,
    val hasNext: Boolean = true,
    val isLoading: Boolean = false
)

@HiltViewModel
class CommentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val commentRepository: CommentRepository
) : ViewModel() {

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
        loadComments()
    }

    // 1. 댓글 목록 조회
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

    fun setEditMode(commentId: Long?) {
        editingCommentId = commentId
    }

    // 2. 댓글 작성 및 수정
    fun submitComment(content: String, onSuccess: () -> Unit) {
        if (content.isBlank()) return

        viewModelScope.launch {
            val targetIdLong = receivedTargetId.toLongOrNull() ?: 0L
            val editId = editingCommentId

            if (editId != null) {
                commentRepository.updateComment(targetIdLong, editId, content)
                    .onSuccess {
                        Log.d(TAG, "🟢 댓글 수정 성공")
                        editingCommentId = null
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

    // 3. 댓글 삭제
    fun deleteComment(commentId: Long) {
        viewModelScope.launch {
            val targetIdLong = receivedTargetId.toLongOrNull() ?: 0L
            commentRepository.deleteComment(targetIdLong, commentId)
                .onSuccess {
                    Log.d(TAG, "🟢 댓글 삭제 성공")
                    loadComments(isRefresh = true)
                }
                .onFailure { Log.e(TAG, "🔴 댓글 삭제 실패", it) }
        }
    }

    // 4. 댓글 좋아요 토글 (등록/취소)
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
}

private fun CommentBoard.toUiModel(): CommentUiModel {
    return CommentUiModel(
        commentId = this.commentId.toString(),
        profileImageUrl = this.user.characterImageUrl,
        nickname = this.user.nickname,
        stance = if (this.stance == "A" || this.stance == "AGREE") "찬성" else "반대",
        content = this.content,
        timeAgo = this.createdAt,
        likeCount = this.likeCount,
        isLiked = this.isLiked,
        isMine = this.isMine
    )
}
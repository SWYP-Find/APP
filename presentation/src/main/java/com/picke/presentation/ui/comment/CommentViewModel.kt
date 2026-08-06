package com.picke.presentation.ui.comment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.feature.comment.usecase.CommentUseCases
import com.picke.domain.feature.comment.usecase.ReportCommentResult
import com.picke.domain.feature.perspective.usecase.PerspectiveUseCases
import com.picke.presentation.analytics.AnalyticsTracker
import com.picke.presentation.ui.comment.model.CommentUiEvent
import com.picke.presentation.ui.comment.model.CommentUiModel
import com.picke.presentation.ui.comment.model.CommentUiState
import com.picke.presentation.ui.comment.model.toUiModel
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

@HiltViewModel
class CommentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val commentUseCases: CommentUseCases,
    private val perspectiveUseCases: PerspectiveUseCases,
    private val analyticsTracker: AnalyticsTracker
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<CommentUiEvent>()
    val uiEvent: SharedFlow<CommentUiEvent> = _uiEvent.asSharedFlow()

    private val receivedTargetId: String = checkNotNull(savedStateHandle["itemId"])
    private val receivedFirstOptionId: Long = savedStateHandle["firstOptionId"] ?: 0L

    private val _uiState = MutableStateFlow(
        CommentUiState(
            targetId = receivedTargetId,
            firstOptionId = receivedFirstOptionId,
            comments = emptyList(),
            hasNext = true
        )
    )
    val uiState: StateFlow<CommentUiState> = _uiState.asStateFlow()

    init {
        loadMainPerspective()
        loadComments()
    }

    // 해당 댓글 단건 조회
    fun loadMainPerspective() {
        viewModelScope.launch {
            val targetIdLong = receivedTargetId.toLongOrNull() ?: 0L

            perspectiveUseCases.loadMainPerspectiveUseCase(targetIdLong)
                .onSuccess { perspective ->
                    _uiState.update { state ->
                        state.copy(
                            mainPerspective = CommentUiModel(
                                commentId = perspective.perspectiveId.toString(),
                                profileImageUrl = perspective.characterImageUrl,
                                nickname = perspective.nickname,
                                stance = perspective.optionTitle,
                                optionId = perspective.optionId,
                                content = perspective.content,
                                timeAgo = perspective.createdAt.toRelativeTimeText(),
                                likeCount = perspective.likeCount,
                                isLiked = perspective.isLiked,
                                isMine = perspective.isMine,
                                replyCount = perspective.commentCount
                            )
                        )
                    }
                }
        }
    }

    fun loadComments(isRefresh: Boolean = false) {
        val state = _uiState.value
        if (state.isLoading || (!isRefresh && !state.hasNext)) return

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val cursor = if (isRefresh) null else state.nextCursor
            val targetIdLong = receivedTargetId.toLongOrNull() ?: 0L

            commentUseCases.loadCommentsUseCase(targetIdLong, cursor, size = 10)
                .onSuccess { page ->
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
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }

    fun submitComment(content: String, onSuccess: () -> Unit) {
        if (content.isBlank()) return

        val targetIdLong = receivedTargetId.toLongOrNull() ?: 0L
        val editId = _uiState.value.editingCommentId
        val isEditMode = editId != null

        _uiState.update { it.copy(editingCommentId = null) }

        viewModelScope.launch {
            commentUseCases.submitCommentUseCase(targetIdLong, editId, content)
                .onSuccess {
                    if (!isEditMode) {
                        analyticsTracker.trackCommunityAction(receivedTargetId, content.length)
                    }
                    onSuccess()
                    loadComments(isRefresh = true)
                }
        }
    }

    fun deleteComment(commentId: Long) {
        _uiState.update { state ->
            state.copy(
                comments = state.comments.filter { it.commentId != commentId.toString() }
            )
        }

        viewModelScope.launch {
            val targetIdLong = receivedTargetId.toLongOrNull() ?: 0L

            commentUseCases.deleteCommentUseCase(targetIdLong, commentId)
                .onSuccess {
                    loadMainPerspective()
                }
                .onFailure { error ->
                    loadComments(isRefresh = true)
                }
        }
    }

    fun toggleLike(commentId: Long, isCurrentlyLiked: Boolean) {
        viewModelScope.launch {
            commentUseCases.toggleCommentLikeUseCase(commentId, isCurrentlyLiked)
                .onSuccess { toggleData ->
                    _uiState.update { state ->
                        state.copy(
                            comments = state.comments.map { item ->
                                if (item.commentId == commentId.toString()) {
                                    item.copy(
                                        likeCount = toggleData.likeCount,
                                        isLiked = toggleData.isLiked
                                    )
                                } else item
                            }
                        )
                    }
                }
        }
    }

    fun toggleMainPerspectiveLike() {
        val mainItem = _uiState.value.mainPerspective ?: return
        viewModelScope.launch {
            val targetIdLong = receivedTargetId.toLongOrNull() ?: 0L

            perspectiveUseCases.togglePerspectiveLikeUseCase(targetIdLong, mainItem.isLiked)
                .onSuccess { toggleData ->
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

    fun setEditMode(commentId: Long?) {
        _uiState.update { it.copy(editingCommentId = commentId) }
    }

    fun reportComment(commentId: Long) {
        viewModelScope.launch {
            val targetIdLong = receivedTargetId.toLongOrNull() ?: 0L

            commentUseCases.reportCommentUseCase(targetIdLong, commentId)
                .onSuccess { result ->
                    when (result) {
                        is ReportCommentResult.Reported -> {
                            _uiEvent.emit(CommentUiEvent.ShowToast("신고가 정상 접수되었습니다."))
                        }

                        is ReportCommentResult.AlreadyReported -> {
                            _uiEvent.emit(CommentUiEvent.ShowToast("이미 신고한 사용자입니다."))
                        }
                    }
                }
        }
    }

    fun refreshAllData() {
        loadMainPerspective()
        loadComments(isRefresh = true)
    }
}
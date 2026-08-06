package com.picke.presentation.ui.comment.model

data class CommentUiState(
    val targetId: String = "",
    val firstOptionId: Long = 0L,
    val mainPerspective: CommentUiModel? = null,
    val comments: List<CommentUiModel> = emptyList(),
    val nextCursor: String? = null,
    val hasNext: Boolean = true,
    val isLoading: Boolean = false,
    val editingCommentId: Long? = null
)
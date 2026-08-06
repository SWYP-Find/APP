package com.picke.presentation.ui.comment.model

sealed class CommentUiEvent {
    data class ShowToast(val message: String) : CommentUiEvent()
}
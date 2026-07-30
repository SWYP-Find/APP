package com.picke.domain.usecase.comment

import com.picke.domain.model.CommentLikeToggleBoard
import com.picke.domain.repository.CommentRepository

class ToggleCommentLikeUseCase(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(
        commentId: Long,
        isCurrentlyLiked: Boolean
    ): Result<CommentLikeToggleBoard> {
        return if (isCurrentlyLiked) {
            commentRepository.unlikeComment(commentId)
        } else {
            commentRepository.likeComment(commentId)
        }
    }
}

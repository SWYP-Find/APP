package com.picke.domain.feature.comment.usecase

import com.picke.domain.feature.comment.model.CommentLikeToggleBoard
import com.picke.domain.feature.comment.repository.CommentRepository

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

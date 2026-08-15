package com.picke.app.domain.usecase.comment

import com.picke.app.domain.model.CommentLikeToggleBoard
import com.picke.app.domain.repository.CommentRepository
import javax.inject.Inject

class ToggleCommentLikeUseCase @Inject constructor(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(commentId: Long, isCurrentlyLiked: Boolean): Result<CommentLikeToggleBoard> {
        return if (isCurrentlyLiked) {
            commentRepository.unlikeComment(commentId)
        } else {
            commentRepository.likeComment(commentId)
        }
    }
}

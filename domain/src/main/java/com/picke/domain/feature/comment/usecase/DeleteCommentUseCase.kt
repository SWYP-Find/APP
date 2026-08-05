package com.picke.domain.feature.comment.usecase

import com.picke.domain.feature.comment.repository.CommentRepository

class DeleteCommentUseCase(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(perspectiveId: Long, commentId: Long): Result<String> {
        return commentRepository.deleteComment(perspectiveId, commentId)
    }
}

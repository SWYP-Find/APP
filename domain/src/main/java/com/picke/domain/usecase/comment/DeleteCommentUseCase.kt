package com.picke.domain.usecase.comment

import com.picke.domain.repository.CommentRepository

class DeleteCommentUseCase(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(perspectiveId: Long, commentId: Long): Result<String> {
        return commentRepository.deleteComment(perspectiveId, commentId)
    }
}

package com.picke.domain.usecase.comment

import com.picke.domain.repository.CommentRepository

class SubmitCommentUseCase(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(perspectiveId: Long, editingCommentId: Long?, content: String): Result<Unit> {
        return if (editingCommentId != null) {
            commentRepository.updateComment(perspectiveId, editingCommentId, content).map { }
        } else {
            commentRepository.createComment(perspectiveId, content).map { }
        }
    }
}

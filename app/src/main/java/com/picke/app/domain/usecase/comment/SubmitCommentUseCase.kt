package com.picke.app.domain.usecase.comment

import com.picke.app.domain.repository.CommentRepository
import javax.inject.Inject

class SubmitCommentUseCase @Inject constructor(
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

package com.picke.app.domain.usecase.comment

import com.picke.app.domain.repository.CommentRepository
import javax.inject.Inject

class DeleteCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(perspectiveId: Long, commentId: Long): Result<String> {
        return commentRepository.deleteComment(perspectiveId, commentId)
    }
}

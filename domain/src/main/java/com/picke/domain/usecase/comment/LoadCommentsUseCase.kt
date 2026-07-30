package com.picke.domain.usecase.comment

import com.picke.domain.model.CommentPageBoard
import com.picke.domain.repository.CommentRepository

class LoadCommentsUseCase(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(
        perspectiveId: Long,
        cursor: String?,
        size: Int = 10
    ): Result<CommentPageBoard> {
        return commentRepository.getComments(perspectiveId, cursor, size)
    }
}

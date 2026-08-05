package com.picke.domain.feature.comment.usecase

import com.picke.domain.feature.comment.model.CommentPageBoard
import com.picke.domain.feature.comment.repository.CommentRepository

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

package com.picke.app.domain.usecase

import com.picke.app.domain.model.CommentPageBoard
import com.picke.app.domain.repository.CommentRepository
import javax.inject.Inject

class LoadCommentsUseCase @Inject constructor(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(perspectiveId: Long, cursor: String?, size: Int = 10): Result<CommentPageBoard> {
        return commentRepository.getComments(perspectiveId, cursor, size)
    }
}

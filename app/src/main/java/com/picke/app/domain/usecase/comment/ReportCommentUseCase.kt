package com.picke.app.domain.usecase.comment

import com.picke.app.domain.repository.CommentRepository
import javax.inject.Inject

sealed class ReportCommentResult {
    data object Reported : ReportCommentResult()
    data object AlreadyReported : ReportCommentResult()
}

class ReportCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(perspectiveId: Long, commentId: Long): Result<ReportCommentResult> {
        return commentRepository.reportComment(perspectiveId, commentId)
            .map { ReportCommentResult.Reported as ReportCommentResult }
            .recoverCatching { error ->
                if (error.message == "ALREADY_REPORTED") {
                    ReportCommentResult.AlreadyReported
                } else {
                    throw error
                }
            }
    }
}

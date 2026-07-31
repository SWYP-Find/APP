package com.picke.domain.usecase.perspective

import com.picke.domain.repository.PerspectiveRepository

sealed class ReportPerspectiveResult {
    data object Reported : ReportPerspectiveResult()
    data object AlreadyReported : ReportPerspectiveResult()
}

class ReportPerspectiveUseCase(
    private val perspectiveRepository: PerspectiveRepository
) {
    suspend operator fun invoke(perspectiveId: Long): Result<ReportPerspectiveResult> {
        return perspectiveRepository.reportPerspective(perspectiveId)
            .map { ReportPerspectiveResult.Reported as ReportPerspectiveResult }
            .recoverCatching { error ->
                if (error.message == "ALREADY_REPORTED") {
                    ReportPerspectiveResult.AlreadyReported
                } else {
                    throw error
                }
            }
    }
}

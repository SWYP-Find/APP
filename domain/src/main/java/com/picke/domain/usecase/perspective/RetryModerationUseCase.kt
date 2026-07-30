package com.picke.domain.usecase.perspective

import com.picke.domain.repository.PerspectiveRepository

class RetryModerationUseCase(
    private val perspectiveRepository: PerspectiveRepository
) {
    suspend operator fun invoke(perspectiveId: Long): Result<String> {
        return perspectiveRepository.retryModeration(perspectiveId)
    }
}

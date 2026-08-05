package com.picke.domain.feature.perspective.usecase

import com.picke.domain.feature.perspective.repository.PerspectiveRepository

class DeletePerspectiveUseCase(
    private val perspectiveRepository: PerspectiveRepository
) {
    suspend operator fun invoke(perspectiveId: Long): Result<String> {
        return perspectiveRepository.deletePerspective(perspectiveId)
    }
}

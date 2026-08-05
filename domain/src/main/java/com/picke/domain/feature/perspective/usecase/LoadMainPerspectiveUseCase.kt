package com.picke.domain.feature.perspective.usecase

import com.picke.domain.feature.perspective.model.PerspectiveDetailBoard
import com.picke.domain.feature.perspective.repository.PerspectiveRepository

class LoadMainPerspectiveUseCase(
    private val perspectiveRepository: PerspectiveRepository
) {
    suspend operator fun invoke(perspectiveId: Long): Result<PerspectiveDetailBoard> {
        return perspectiveRepository.getPerspective(perspectiveId)
    }
}

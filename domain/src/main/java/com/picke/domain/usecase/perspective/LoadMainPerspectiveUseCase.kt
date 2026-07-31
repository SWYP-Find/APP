package com.picke.domain.usecase.perspective

import com.picke.domain.model.PerspectiveDetailBoard
import com.picke.domain.repository.PerspectiveRepository

class LoadMainPerspectiveUseCase(
    private val perspectiveRepository: PerspectiveRepository
) {
    suspend operator fun invoke(perspectiveId: Long): Result<PerspectiveDetailBoard> {
        return perspectiveRepository.getPerspective(perspectiveId)
    }
}

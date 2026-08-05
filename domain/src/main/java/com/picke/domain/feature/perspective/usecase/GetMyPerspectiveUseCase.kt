package com.picke.domain.feature.perspective.usecase

import com.picke.domain.feature.perspective.model.PerspectiveDetailBoard
import com.picke.domain.feature.perspective.repository.PerspectiveRepository

class GetMyPerspectiveUseCase(
    private val perspectiveRepository: PerspectiveRepository
) {
    suspend operator fun invoke(battleId: Long): Result<PerspectiveDetailBoard> {
        return perspectiveRepository.getMyPerspective(battleId)
    }
}

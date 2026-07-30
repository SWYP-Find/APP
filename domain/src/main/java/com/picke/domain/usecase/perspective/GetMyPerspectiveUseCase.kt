package com.picke.domain.usecase.perspective

import com.picke.domain.model.PerspectiveDetailBoard
import com.picke.domain.repository.PerspectiveRepository

class GetMyPerspectiveUseCase(
    private val perspectiveRepository: PerspectiveRepository
) {
    suspend operator fun invoke(battleId: Long): Result<PerspectiveDetailBoard> {
        return perspectiveRepository.getMyPerspective(battleId)
    }
}

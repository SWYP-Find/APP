package com.picke.app.domain.usecase

import com.picke.app.domain.model.PerspectiveDetailBoard
import com.picke.app.domain.repository.PerspectiveRepository
import javax.inject.Inject

class GetMyPerspectiveUseCase @Inject constructor(
    private val perspectiveRepository: PerspectiveRepository
) {
    suspend operator fun invoke(battleId: Long): Result<PerspectiveDetailBoard> {
        return perspectiveRepository.getMyPerspective(battleId)
    }
}

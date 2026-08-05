package com.picke.domain.feature.perspective.usecase

import com.picke.domain.feature.perspective.model.PerspectivePage
import com.picke.domain.feature.perspective.repository.PerspectiveRepository

class LoadPerspectivesUseCase(
    private val perspectiveRepository: PerspectiveRepository
) {
    suspend operator fun invoke(
        battleId: Long,
        cursor: String?,
        size: Int = 10,
        optionId: Long?,
        sort: String
    ): Result<PerspectivePage> {
        return perspectiveRepository.getPerspectives(
            battleId = battleId,
            cursor = cursor,
            size = size,
            optionId = optionId,
            sort = sort
        )
    }
}

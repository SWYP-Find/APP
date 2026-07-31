package com.picke.domain.usecase.perspective

import com.picke.domain.model.PerspectivePage
import com.picke.domain.repository.PerspectiveRepository

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

package com.picke.app.domain.usecase.perspective

import com.picke.app.domain.model.PerspectivePage
import com.picke.app.domain.repository.PerspectiveRepository
import javax.inject.Inject

class LoadPerspectivesUseCase @Inject constructor(
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

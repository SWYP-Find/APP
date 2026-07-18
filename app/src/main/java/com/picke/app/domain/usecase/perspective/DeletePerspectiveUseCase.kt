package com.picke.app.domain.usecase.perspective

import com.picke.app.domain.repository.PerspectiveRepository
import javax.inject.Inject

class DeletePerspectiveUseCase @Inject constructor(
    private val perspectiveRepository: PerspectiveRepository
) {
    suspend operator fun invoke(perspectiveId: Long): Result<String> {
        return perspectiveRepository.deletePerspective(perspectiveId)
    }
}

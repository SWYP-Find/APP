package com.picke.app.domain.usecase

import com.picke.app.domain.repository.PerspectiveRepository
import javax.inject.Inject

class SubmitPerspectiveUseCase @Inject constructor(
    private val perspectiveRepository: PerspectiveRepository
) {
    suspend operator fun invoke(battleId: Long, editingPerspectiveId: Long?, content: String): Result<Unit> {
        return if (editingPerspectiveId != null) {
            perspectiveRepository.updatePerspective(editingPerspectiveId, content).map { }
        } else {
            perspectiveRepository.createPerspective(battleId, content).map { }
        }
    }
}

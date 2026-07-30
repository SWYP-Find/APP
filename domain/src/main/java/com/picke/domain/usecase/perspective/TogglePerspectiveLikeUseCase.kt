package com.picke.domain.usecase.perspective

import com.picke.domain.model.PerspectiveLikeToggleBoard
import com.picke.domain.repository.PerspectiveRepository

class TogglePerspectiveLikeUseCase(
    private val perspectiveRepository: PerspectiveRepository
) {
    suspend operator fun invoke(
        perspectiveId: Long,
        isCurrentlyLiked: Boolean
    ): Result<PerspectiveLikeToggleBoard> {
        return if (isCurrentlyLiked) {
            perspectiveRepository.unlikePerspective(perspectiveId)
        } else {
            perspectiveRepository.likePerspective(perspectiveId)
        }
    }
}

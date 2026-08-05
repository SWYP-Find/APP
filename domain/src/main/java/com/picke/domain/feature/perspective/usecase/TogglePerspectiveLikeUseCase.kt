package com.picke.domain.feature.perspective.usecase

import com.picke.domain.feature.perspective.model.PerspectiveLikeToggleBoard
import com.picke.domain.feature.perspective.repository.PerspectiveRepository

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

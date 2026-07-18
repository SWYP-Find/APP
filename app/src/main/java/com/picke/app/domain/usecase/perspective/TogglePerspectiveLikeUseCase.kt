package com.picke.app.domain.usecase.perspective

import com.picke.app.domain.model.PerspectiveLikeToggleBoard
import com.picke.app.domain.repository.PerspectiveRepository
import javax.inject.Inject

class TogglePerspectiveLikeUseCase @Inject constructor(
    private val perspectiveRepository: PerspectiveRepository
) {
    suspend operator fun invoke(perspectiveId: Long, isCurrentlyLiked: Boolean): Result<PerspectiveLikeToggleBoard> {
        return if (isCurrentlyLiked) {
            perspectiveRepository.unlikePerspective(perspectiveId)
        } else {
            perspectiveRepository.likePerspective(perspectiveId)
        }
    }
}

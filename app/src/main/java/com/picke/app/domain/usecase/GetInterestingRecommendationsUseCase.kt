package com.picke.app.domain.usecase

import com.picke.app.domain.model.RecommendPageBoard
import com.picke.app.domain.repository.RecommendRepository
import javax.inject.Inject

class GetInterestingRecommendationsUseCase @Inject constructor(
    private val recommendRepository: RecommendRepository
) {
    suspend operator fun invoke(battleId: Long): Result<RecommendPageBoard> {
        return recommendRepository.getInterestingRecommendations(battleId)
    }
}

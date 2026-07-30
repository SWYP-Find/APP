package com.picke.domain.usecase.recommend

import com.picke.domain.model.RecommendPageBoard
import com.picke.domain.repository.RecommendRepository

class GetInterestingRecommendationsUseCase(
    private val recommendRepository: RecommendRepository
) {
    suspend operator fun invoke(battleId: Long): Result<RecommendPageBoard> {
        return recommendRepository.getInterestingRecommendations(battleId)
    }
}

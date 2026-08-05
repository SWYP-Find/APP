package com.picke.domain.feature.recommend.usecase

import com.picke.domain.feature.recommend.model.RecommendPageBoard
import com.picke.domain.feature.recommend.repository.RecommendRepository

class GetInterestingRecommendationsUseCase(
    private val recommendRepository: RecommendRepository
) {
    suspend operator fun invoke(battleId: Long): Result<RecommendPageBoard> {
        return recommendRepository.getInterestingRecommendations(battleId)
    }
}

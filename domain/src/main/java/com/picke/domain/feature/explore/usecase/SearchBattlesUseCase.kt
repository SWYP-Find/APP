package com.picke.domain.feature.explore.usecase

import com.picke.domain.feature.explore.model.ExplorePageBoard
import com.picke.domain.feature.explore.repository.ExploreRepository

class SearchBattlesUseCase(
    private val exploreRepository: ExploreRepository
) {
    suspend operator fun invoke(
        category: String?,
        sort: String,
        offset: Int?,
        size: Int
    ): Result<ExplorePageBoard> {
        return exploreRepository.searchBattles(category, sort, offset, size)
    }
}

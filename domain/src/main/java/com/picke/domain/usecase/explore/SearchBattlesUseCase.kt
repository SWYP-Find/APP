package com.picke.domain.usecase.explore

import com.picke.domain.model.ExplorePageBoard
import com.picke.domain.repository.ExploreRepository

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

package com.picke.app.domain.usecase

import com.picke.app.domain.model.ExplorePageBoard
import com.picke.app.domain.repository.ExploreRepository
import javax.inject.Inject

class SearchBattlesUseCase @Inject constructor(
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

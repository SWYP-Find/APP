package com.picke.domain.feature.explore.repository

import com.picke.domain.feature.explore.model.ExplorePageBoard

interface ExploreRepository {
    suspend fun searchBattles(
        category: String? = null,
        sort: String = "LATEST",
        offset: Int? = null,
        size: Int = 10
    ): Result<ExplorePageBoard>
}
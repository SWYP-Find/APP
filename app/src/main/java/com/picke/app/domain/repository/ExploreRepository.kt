package com.picke.app.domain.repository

import com.picke.app.domain.model.ExplorePageBoard

interface ExploreRepository {
    suspend fun searchBattles(
        category: String? = null,
        sort: String = "LATEST",
        offset: Int? = null,
        size: Int = 10
    ): Result<ExplorePageBoard>
}
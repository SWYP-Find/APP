package com.swyp4.team2.domain.repository

import androidx.paging.PagingData
import com.swyp4.team2.domain.model.ExplorePageBoard
import kotlinx.coroutines.flow.Flow

interface ExploreRepository {
    suspend fun searchBattles(
        category: String? = null,
        sort: String = "LATEST",
        offset: Int? = null,
        size: Int = 10
    ): Result<ExplorePageBoard>
}
package com.swyp4.team2.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.swyp4.team2.data.remote.ExploreApi
import com.swyp4.team2.data.remote.ExplorePagingSource
import com.swyp4.team2.domain.model.ContentCard
import com.swyp4.team2.domain.repository.ExploreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExploreRepositoryImpl @Inject constructor(
    private val api: ExploreApi
) : ExploreRepository{
    override fun getExploreContent(
        category: String,
        sortOption: String
    ): Flow<PagingData<ContentCard>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ExplorePagingSource(api, category) }
        ).flow
    }
}
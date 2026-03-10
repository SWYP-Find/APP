package com.swyp4.team2.domain.repository

import androidx.paging.PagingData
import com.swyp4.team2.domain.model.ContentCard
import kotlinx.coroutines.flow.Flow

interface ExploreRepository {
    fun getExploreContent(category:String, sortOption: String): Flow<PagingData<ContentCard>>
}
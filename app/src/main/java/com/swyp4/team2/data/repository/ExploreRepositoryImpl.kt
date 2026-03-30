package com.swyp4.team2.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.swyp4.team2.data.model.toDomainModel
import com.swyp4.team2.data.remote.ExploreApi
import com.swyp4.team2.ui.explore.ExplorePagingSource

import com.swyp4.team2.domain.model.ExplorePageBoard
import com.swyp4.team2.domain.repository.ExploreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExploreRepositoryImpl @Inject constructor(
    private val exploreApi: ExploreApi
) : ExploreRepository{
    override suspend fun searchBattles(
        category: String?,
        sort: String,
        offset: Int?,
        size: Int
    ): Result<ExplorePageBoard> {
        return try {
            val response = exploreApi.searchBattles(category, sort, offset, size)

            // 데이터가 없으면 예외 처리, 있으면 성공 처리
            val data = response.data ?: throw Exception(response.error?.message ?: "탐색 목록을 불러오지 못했습니다.")
            Result.success(data.toDomainModel())

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
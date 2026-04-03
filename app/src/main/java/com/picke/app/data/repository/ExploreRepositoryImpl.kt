package com.picke.app.data.repository

import com.picke.app.data.model.toDomainModel
import com.picke.app.data.remote.ExploreApi

import com.picke.app.domain.model.ExplorePageBoard
import com.picke.app.domain.repository.ExploreRepository
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
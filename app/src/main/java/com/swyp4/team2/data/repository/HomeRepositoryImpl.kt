package com.swyp4.team2.data.repository

import com.swyp4.team2.data.model.toDomainModel
import com.swyp4.team2.data.model.toTodayPickDomainModel
import com.swyp4.team2.data.remote.HomeApi
import com.swyp4.team2.domain.mock.DummyHomeData
import com.swyp4.team2.domain.model.HomeBoard
import com.swyp4.team2.domain.repository.HomeRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeApi: HomeApi
) : HomeRepository {

    override suspend fun fetchHomeData(): Result<HomeBoard> {
        return try {
            val response = homeApi.getHomeData()
            val responseData = response.data ?: throw Exception(response.error?.message ?: "데이터를 불러올 수 없습니다.")

            val boardData = HomeBoard(
                hasNewNotice = responseData.newNotice,
                editorPicks = responseData.editorPicks.map { it.toDomainModel() },
                trendingBattles = responseData.trendingBattles.map { it.toDomainModel() },
                bestBattles = responseData.bestBattles.map { it.toDomainModel() },
                todayPicks = responseData.todayPicks.mapNotNull { it.toTodayPickDomainModel() },
                newBattles = responseData.newBattles.map { it.toDomainModel() }
            )

            Result.success(boardData)
            // return Result.success(DummyHomeData.getDummyBoardData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
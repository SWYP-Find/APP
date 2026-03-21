package com.swyp4.team2.data.repository

import com.swyp4.team2.data.mapper.toHomeContentModel
import com.swyp4.team2.data.mapper.toTodayPickModel
import com.swyp4.team2.data.remote.HomeApi
import com.swyp4.team2.domain.mock.DummyHomeData
import com.swyp4.team2.domain.model.HomeBoardData
import com.swyp4.team2.domain.repository.HomeRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeApi: HomeApi
) : HomeRepository {

    override suspend fun fetchHomeData(): Result<HomeBoardData> {
        return try {
            // 1. 서버에서 DTO 받아오기
            // val response = homeApi.getHomeData()

            // 2. Mapper를 통해 DTO -> UI Model 변환 및 포장
            /*
            val boardData = HomeBoardData(
                hasNewNotice = response.newNotice,
                editorPicks = response.editorPicks.map { it.toHomeContentModel() },
                trendingBattles = response.trendingBattles.map { it.toHomeContentModel() },
                bestBattles = response.bestBattles.map { it.toHomeContentModel() },
                todayPicks = response.todayPicks.mapNotNull { it.toTodayPickModel() },
                newBattles = response.newBattles.map { it.toHomeContentModel() }
            )
             */

            // 🌟 3. 성공 시 Result.success 로 감싸서 반환 (이게 무조건 마지막 줄이어야 함!)
            // Result.success(boardData)
            return Result.success(DummyHomeData.getDummyBoardData)
        } catch (e: Exception) {
            // 4. 네트워크 에러 등 예외 발생 시 Result.failure 로 반환
            Result.failure(e)
        }
    }
}
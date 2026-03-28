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
                // 1. Boolean? 타입을 Boolean으로 (없으면 false)
                hasNewNotice = responseData.newNotice ?: false,

                // 2. 리스트가 null이면 emptyList()를 반환하도록 안전하게 처리
                editorPicks = responseData.editorPicks?.map { it.toDomainModel() } ?: emptyList(),
                trendingBattles = responseData.trendingBattles?.map { it.toDomainModel() } ?: emptyList(),
                bestBattles = responseData.bestBattles?.map { it.toDomainModel() } ?: emptyList(),

                // 3. 오늘의 픽(퀴즈+투표)도 각각 null 체크 후 합쳐주기
                todayPicks = (responseData.todayQuizzes?.map { it.toTodayPickDomainModel() } ?: emptyList()) +
                        (responseData.todayVotes?.map { it.toTodayPickDomainModel() } ?: emptyList()),

                newBattles = responseData.newBattles?.map { it.toDomainModel() } ?: emptyList()
            )

            Result.success(boardData)
            // return Result.success(DummyHomeData.getDummyBoardData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
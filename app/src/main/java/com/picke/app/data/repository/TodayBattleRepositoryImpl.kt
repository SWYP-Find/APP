package com.picke.app.data.repository

import com.picke.app.data.model.toDomainModel
import com.picke.app.data.remote.TodayBattleApi
import com.picke.app.domain.model.TodayBattleBoard
import com.picke.app.domain.repository.TodayBattleRepository
import javax.inject.Inject

class TodayBattleRepositoryImpl @Inject constructor(
    private val battleApi: TodayBattleApi
) : TodayBattleRepository {
    override suspend fun fetchTodayBattles(): Result<TodayBattleBoard> {
        return try {
            val response = battleApi.getTodayBattles()
            val responseData = response.data

            if (response.statusCode == 200 && responseData != null) {
                Result.success(responseData.toDomainModel())
            } else {
                val errorMessage = response.error?.message ?: "오늘의 배틀을 불러오는데 실패했습니다."
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
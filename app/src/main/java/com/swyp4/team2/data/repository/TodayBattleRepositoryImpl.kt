package com.swyp4.team2.data.repository

import android.util.Log
import com.swyp4.team2.data.model.toDomainModel
import com.swyp4.team2.data.remote.TodayBattleApi
import com.swyp4.team2.domain.model.TodayBattleBoard
import com.swyp4.team2.domain.repository.TodayBattleRepository
import javax.inject.Inject

class TodayBattleRepositoryImpl @Inject constructor(
    private val battleApi: TodayBattleApi
) : TodayBattleRepository {
    override suspend fun fetchTodayBattles(): Result<TodayBattleBoard> {
        return try {
            val response = battleApi.getTodayBattles()
            val responseData = response.data

            Log.d("ApiDebug", "서버 응답 상태코드: ${response.statusCode}")
            Log.d("ApiDebug", "서버 응답 데이터: ${response.data}")
            Log.d("ApiDebug", "서버 에러 메시지: ${response.error}")

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
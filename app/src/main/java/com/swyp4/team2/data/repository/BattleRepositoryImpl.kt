package com.swyp4.team2.data.repository

import com.swyp4.team2.data.model.toDomainModel
import com.swyp4.team2.data.remote.BattleApi
import com.swyp4.team2.domain.model.BattleDetailBoard
import com.swyp4.team2.domain.repository.BattleRepository
import javax.inject.Inject

class BattleRepositoryImpl @Inject constructor(
    private val battleApi: BattleApi
): BattleRepository {

    override suspend fun getBattleDetail(battleId: Long): Result<BattleDetailBoard> {
        return try {
            val response = battleApi.getBattleDetail(battleId)
            val responseData = response.data

            if (response.statusCode == 200 && responseData != null) {
                Result.success(responseData.toDomainModel())
            } else {
                val errorMessage = response.error?.message ?: "배틀 상세 정보를 불러오지 못했습니다."
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
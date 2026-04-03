package com.picke.app.data.repository

import com.picke.app.data.model.toDomainModel
import com.picke.app.data.remote.BattleApi
import com.picke.app.domain.model.BattleDetailBoard
import com.picke.app.domain.model.BattleStatusBoard
import com.picke.app.domain.repository.BattleRepository
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

    override suspend fun getBattleStatus(battleId: Long): Result<BattleStatusBoard> {
        return try {
            val response = battleApi.getBattleStatus(battleId)
            val responseData = response.data

            if ((response.statusCode == 200 || response.statusCode == 0) && responseData != null) {
                Result.success(responseData.toDomainModel())
            } else {
                val errorMessage = response.error?.message ?: "배틀 진행 상태를 불러오지 못했습니다."
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
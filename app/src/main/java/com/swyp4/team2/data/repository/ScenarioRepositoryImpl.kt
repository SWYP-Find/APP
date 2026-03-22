package com.swyp4.team2.data.repository

import com.swyp4.team2.data.model.toDomainModel
import com.swyp4.team2.data.remote.ScenarioApi
import com.swyp4.team2.domain.model.ScenarioBoard
import com.swyp4.team2.domain.repository.ScenarioRepository
import javax.inject.Inject

class ScenarioRepositoryImpl @Inject constructor(
    private val scenarioApi: ScenarioApi
): ScenarioRepository {
    override suspend fun fetchBattleScenario(battleId: String): Result<ScenarioBoard> {
        return try {
            val response = scenarioApi.getScenario(battleId)
            val responseData = response.data

            if (response.statusCode == 200 && responseData != null) {
                Result.success(responseData.toDomainModel())
            } else {
                val errorMessage = response.error?.message ?: "시나리오를 불러오지 못했습니다."
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
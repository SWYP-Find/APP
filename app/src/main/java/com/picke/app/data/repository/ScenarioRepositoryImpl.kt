package com.picke.app.data.repository

import com.picke.app.data.model.toResult
import com.picke.app.data.model.toDomainModel
import com.picke.app.data.remote.ScenarioApi
import com.picke.app.domain.model.ScenarioBoard
import com.picke.app.domain.repository.ScenarioRepository
import javax.inject.Inject

class ScenarioRepositoryImpl @Inject constructor(
    private val scenarioApi: ScenarioApi
) : ScenarioRepository {
    override suspend fun fetchBattleScenario(battleId: String): Result<ScenarioBoard> {
        return try {
            scenarioApi.getScenario(battleId)
                .toResult("시나리오를 불러오지 못했습니다.")
                .map { it.toDomainModel() }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
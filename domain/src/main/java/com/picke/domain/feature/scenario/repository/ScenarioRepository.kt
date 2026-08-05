package com.picke.domain.feature.scenario.repository

import com.picke.domain.feature.scenario.model.ScenarioBoard

interface ScenarioRepository {
    suspend fun fetchBattleScenario(battleId: String): Result<ScenarioBoard>
}
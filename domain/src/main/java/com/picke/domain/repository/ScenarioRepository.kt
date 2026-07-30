package com.picke.domain.repository

import com.picke.domain.model.ScenarioBoard

interface ScenarioRepository {
    suspend fun fetchBattleScenario(battleId: String): Result<ScenarioBoard>
}
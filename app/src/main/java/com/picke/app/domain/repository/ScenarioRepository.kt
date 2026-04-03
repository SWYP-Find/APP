package com.picke.app.domain.repository

import com.picke.app.domain.model.ScenarioBoard

interface ScenarioRepository {
    suspend fun fetchBattleScenario(battleId: String): Result<ScenarioBoard>
}
package com.swyp4.team2.domain.repository

import com.swyp4.team2.domain.model.ScenarioBoard

interface ScenarioRepository {
    suspend fun fetchBattleScenario(battleId: String): Result<ScenarioBoard>
}
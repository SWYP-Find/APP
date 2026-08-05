package com.picke.domain.feature.scenario.usecase

import com.picke.domain.feature.scenario.model.ScenarioBoard
import com.picke.domain.feature.scenario.repository.ScenarioRepository

class FetchBattleScenarioUseCase(
    private val scenarioRepository: ScenarioRepository
) {
    suspend operator fun invoke(battleId: String): Result<ScenarioBoard> {
        return scenarioRepository.fetchBattleScenario(battleId)
    }
}

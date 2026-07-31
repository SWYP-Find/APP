package com.picke.domain.usecase.scenario

import com.picke.domain.model.ScenarioBoard
import com.picke.domain.repository.ScenarioRepository

class FetchBattleScenarioUseCase(
    private val scenarioRepository: ScenarioRepository
) {
    suspend operator fun invoke(battleId: String): Result<ScenarioBoard> {
        return scenarioRepository.fetchBattleScenario(battleId)
    }
}

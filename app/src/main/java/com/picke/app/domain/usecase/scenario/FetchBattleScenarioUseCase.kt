package com.picke.app.domain.usecase.scenario

import com.picke.app.domain.model.ScenarioBoard
import com.picke.app.domain.repository.ScenarioRepository
import javax.inject.Inject

class FetchBattleScenarioUseCase @Inject constructor(
    private val scenarioRepository: ScenarioRepository
) {
    suspend operator fun invoke(battleId: String): Result<ScenarioBoard> {
        return scenarioRepository.fetchBattleScenario(battleId)
    }
}

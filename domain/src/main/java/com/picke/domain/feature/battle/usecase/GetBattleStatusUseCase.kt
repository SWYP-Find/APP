package com.picke.domain.feature.battle.usecase

import com.picke.domain.feature.battle.model.BattleStatusBoard
import com.picke.domain.feature.battle.repository.BattleRepository

class GetBattleStatusUseCase(
    private val battleRepository: BattleRepository
) {
    suspend operator fun invoke(battleId: Long): Result<BattleStatusBoard> {
        return battleRepository.getBattleStatus(battleId)
    }
}

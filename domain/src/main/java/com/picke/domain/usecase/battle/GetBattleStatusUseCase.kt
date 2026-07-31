package com.picke.domain.usecase.battle

import com.picke.domain.model.BattleStatusBoard
import com.picke.domain.repository.BattleRepository

class GetBattleStatusUseCase(
    private val battleRepository: BattleRepository
) {
    suspend operator fun invoke(battleId: Long): Result<BattleStatusBoard> {
        return battleRepository.getBattleStatus(battleId)
    }
}

package com.picke.domain.feature.battle.usecase

import com.picke.domain.feature.battle.model.BattleDetailBoard
import com.picke.domain.feature.battle.repository.BattleRepository

class GetBattleDetailUseCase(
    private val battleRepository: BattleRepository
) {
    suspend operator fun invoke(battleId: Long): Result<BattleDetailBoard> {
        return battleRepository.getBattleDetail(battleId)
    }
}

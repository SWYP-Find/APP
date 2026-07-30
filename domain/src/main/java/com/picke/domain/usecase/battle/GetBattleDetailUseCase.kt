package com.picke.domain.usecase.battle

import com.picke.domain.model.BattleDetailBoard
import com.picke.domain.repository.BattleRepository

class GetBattleDetailUseCase(
    private val battleRepository: BattleRepository
) {
    suspend operator fun invoke(battleId: Long): Result<BattleDetailBoard> {
        return battleRepository.getBattleDetail(battleId)
    }
}

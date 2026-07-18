package com.picke.app.domain.usecase.battle

import com.picke.app.domain.model.BattleStatusBoard
import com.picke.app.domain.repository.BattleRepository
import javax.inject.Inject

class GetBattleStatusUseCase @Inject constructor(
    private val battleRepository: BattleRepository
) {
    suspend operator fun invoke(battleId: Long): Result<BattleStatusBoard> {
        return battleRepository.getBattleStatus(battleId)
    }
}

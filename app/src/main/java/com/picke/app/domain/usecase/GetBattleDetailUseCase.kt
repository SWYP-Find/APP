package com.picke.app.domain.usecase

import com.picke.app.domain.model.BattleDetailBoard
import com.picke.app.domain.repository.BattleRepository
import javax.inject.Inject

class GetBattleDetailUseCase @Inject constructor(
    private val battleRepository: BattleRepository
) {
    suspend operator fun invoke(battleId: Long): Result<BattleDetailBoard> {
        return battleRepository.getBattleDetail(battleId)
    }
}

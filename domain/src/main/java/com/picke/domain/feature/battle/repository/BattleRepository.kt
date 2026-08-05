package com.picke.domain.feature.battle.repository

import com.picke.domain.feature.battle.model.BattleDetailBoard
import com.picke.domain.feature.battle.model.BattleStatusBoard

interface BattleRepository {
    suspend fun getBattleDetail(battleId: Long): Result<BattleDetailBoard>
    suspend fun getBattleStatus(battleId: Long): Result<BattleStatusBoard>
}
package com.picke.domain.repository

import com.picke.domain.model.BattleDetailBoard
import com.picke.domain.model.BattleStatusBoard

interface BattleRepository {
    suspend fun getBattleDetail(battleId: Long): Result<BattleDetailBoard>
    suspend fun getBattleStatus(battleId: Long): Result<BattleStatusBoard>
}
package com.picke.app.domain.repository

import com.picke.app.domain.model.BattleDetailBoard
import com.picke.app.domain.model.BattleStatusBoard

interface BattleRepository {
    suspend fun getBattleDetail(battleId: Long): Result<BattleDetailBoard>
    suspend fun getBattleStatus(battleId: Long): Result<BattleStatusBoard>
}
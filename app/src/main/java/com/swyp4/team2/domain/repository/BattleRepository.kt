package com.swyp4.team2.domain.repository

import com.swyp4.team2.domain.model.BattleDetailBoard
import com.swyp4.team2.domain.model.BattleStatusBoard

interface BattleRepository {
    suspend fun getBattleDetail(battleId: Long): Result<BattleDetailBoard>
    suspend fun getBattleStatus(battleId: Long): Result<BattleStatusBoard>
}
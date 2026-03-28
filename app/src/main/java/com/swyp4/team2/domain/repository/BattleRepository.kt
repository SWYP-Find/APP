package com.swyp4.team2.domain.repository

import com.swyp4.team2.domain.model.BattleDetailBoard

interface BattleRepository {
    suspend fun getBattleDetail(battleId: Long): Result<BattleDetailBoard>
}
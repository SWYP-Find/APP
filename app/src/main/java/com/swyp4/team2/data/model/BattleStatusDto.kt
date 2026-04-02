package com.swyp4.team2.data.model

import com.swyp4.team2.domain.model.BattleStatusBoard

data class BattleStatusDto(
    val battleId: Long?,
    val step: String?
)

fun BattleStatusDto.toDomainModel() = BattleStatusBoard(
    battleId = this.battleId ?: 0L,
    step = this.step ?: "NONE"
)
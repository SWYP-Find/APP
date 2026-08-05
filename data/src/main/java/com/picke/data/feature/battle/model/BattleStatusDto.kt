package com.picke.data.feature.battle.model

import com.picke.domain.feature.battle.model.BattleStatusBoard

data class BattleStatusDto(
    val battleId: Long?,
    val step: String?
)

fun BattleStatusDto.toDomainModel() = BattleStatusBoard(
    battleId = this.battleId ?: 0L,
    step = this.step ?: "NONE"
)
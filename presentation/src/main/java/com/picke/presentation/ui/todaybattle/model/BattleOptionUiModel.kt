package com.picke.presentation.ui.todaybattle.model

import com.picke.domain.feature.todaybattle.model.TodayBattleOption

data class BattleOptionUiModel(
    val optionId: String,
    val name: String,
    val opinion: String,
    val quote: String
)

fun TodayBattleOption.toUiModel() = BattleOptionUiModel(
    optionId = optionId,
    name = representative,
    opinion = title,
    quote = stance
)
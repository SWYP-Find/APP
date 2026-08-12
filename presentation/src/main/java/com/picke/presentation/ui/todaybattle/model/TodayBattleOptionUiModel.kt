package com.picke.presentation.ui.todaybattle.model

import com.picke.domain.feature.todaybattle.model.TodayBattleOption

data class TodayBattleOptionUiModel(
    val optionId: String,
    val name: String,
    val opinion: String,
    val quote: String
)

fun TodayBattleOption.toUiModel() = TodayBattleOptionUiModel(
    optionId = optionId,
    name = representative,
    opinion = title,
    quote = stance
)
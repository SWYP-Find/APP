package com.picke.presentation.ui.todaybattle.model

import com.picke.domain.feature.todaybattle.model.TodayBattleItem

data class TodayBattleUiModel(
    val battleId: String,
    val imageUrl: String,
    val tags: List<String>,
    val title: String,
    val description: String,
    val timeLeft: String,
    val options: List<TodayBattleOptionUiModel>
)

fun TodayBattleItem.toUiModel(): TodayBattleUiModel {
    val minutes = (this.audioDuration + 59) / 60

    return TodayBattleUiModel(
        battleId = battleId,
        imageUrl = thumbnailUrl,
        tags = tags,
        title = title,
        description = summary,
        timeLeft = "${minutes}분",
        options = options.map { it.toUiModel() }
    )
}

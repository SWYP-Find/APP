package com.swyp4.team2.ui.todaybattle.model

import com.swyp4.team2.domain.model.TodayBattleItem

data class TodayBattleUiModel(
    val battleId: String,
    val imageUrl: String,
    val tags: List<String>,
    val title: String,
    val description: String,
    val timeLeft: String,
    val options: List<BattleOptionUiModel>
)

data class BattleOptionUiModel(
    val optionId: String,
    val name: String,
    val opinion: String,
    val quote: String
)

fun TodayBattleItem.toUiModel(): TodayBattleUiModel {
    val minutes = (this.audioDuration + 59) / 60

    return TodayBattleUiModel(
        battleId = this.battleId,
        imageUrl = this.thumbnailUrl,
        tags = this.tags,
        title = this.title,
        description = this.summary,
        timeLeft = "${minutes}분",
        options = this.options.map {
            BattleOptionUiModel(
                optionId = it.optionId,
                name = it.representative,
                opinion = it.title,
                quote = it.stance
            )
        }
    )
}

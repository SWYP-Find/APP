package com.swyp4.team2.ui.explore

import com.swyp4.team2.domain.model.ExploreItemBoard

data class ExploreUiModel(
    val battleId: String,
    val thumbnailUrl: String,
    val type: String,
    val title: String,
    val summary: String,
    val tags: List<String>,
    val audioDurationText: String,
    val viewCountText: String
)

// Domain -> UI 매퍼
fun ExploreItemBoard.toUiModel(): ExploreUiModel {
    return ExploreUiModel(
        battleId = this.battleId.toString(),
        thumbnailUrl = this.thumbnailUrl,
        type = if (this.type == "BATTLE") "배틀" else this.type,
        title = this.title,
        summary = this.summary,
        tags = this.tags.map { it.name },
        audioDurationText = "${this.audioDuration/60}분",
        viewCountText = this.viewCount.toString()
    )
}

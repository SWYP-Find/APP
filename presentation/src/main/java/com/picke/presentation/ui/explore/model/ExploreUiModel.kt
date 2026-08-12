package com.picke.presentation.ui.explore.model

import com.picke.domain.feature.explore.model.ExploreItemBoard

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

fun ExploreItemBoard.toUiModel(): ExploreUiModel {
    return ExploreUiModel(
        battleId = battleId.toString(),
        thumbnailUrl = thumbnailUrl,
        type = if (type == "BATTLE") "배틀" else type,
        title = title,
        summary = summary,
        tags = tags.map { it.name },
        audioDurationText = "${audioDuration / 60}분",
        viewCountText = viewCount.toString()
    )
}
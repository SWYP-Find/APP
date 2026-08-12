package com.picke.presentation.ui.recommend.model

import com.picke.domain.feature.recommend.model.RecommendBoard

data class RecommendUiModel(
    val battleId: String,
    val title: String,
    val summary: String,
    val audioDuration: Int,
    val viewCount: Int,
    val participantsCount: Int,
    val tags: List<String>,
    val imageA: String,
    val imageB: String,
    val stanceA: String,
    val stanceB: String,
    val representativeA: String,
    val representativeB: String
)

fun RecommendBoard.toUiModel(): RecommendUiModel {
    val optA = this.options.getOrNull(0)
    val optB = this.options.getOrNull(1)
    val durationInMinutes = if (this.audioDuration in 1..<60) {
        1
    } else {
        this.audioDuration / 60
    }
    return RecommendUiModel(
        battleId = this.battleId.toString(),
        title = this.title,
        summary = this.summary,
        audioDuration = durationInMinutes,
        viewCount = this.viewCount,
        participantsCount = this.participantsCount,
        tags = this.tags.map { it.name },
        imageA = optA?.imageUrl ?: "",
        imageB = optB?.imageUrl ?: "",
        stanceA = optA?.stance ?: "",
        stanceB = optB?.stance ?: "",
        representativeA = optA?.representative ?: "",
        representativeB = optB?.representative ?: ""
    )
}
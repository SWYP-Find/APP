package com.picke.presentation.ui.home.model

import com.picke.domain.feature.home.model.ContentDomainType
import com.picke.domain.feature.home.model.HomeContent

data class HomeContentUiModel(
    val contentId: String,
    val type: ContentUiType,
    val title: String,
    val summary: String,
    val thumbnailUrl: String,
    val viewCountText: String,
    val timeInfoText: String,
    val tags: List<String>,
    val leftOpinion: String? = null,
    val leftProfileName: String? = null,
    val leftProfileImageUrl: String? = null,
    val rightOpinion: String? = null,
    val rightProfileName: String? = null,
    val rightProfileImageUrl: String? = null
)

fun HomeContent.toUiModel(): HomeContentUiModel {
    return HomeContentUiModel(
        contentId = contentId,
        title = title,
        summary = summary,
        thumbnailUrl = thumbnailUrl,
        viewCountText = viewCount.toString(),
        timeInfoText = "${audioDuration / 60}분",
        tags = tags,
        leftOpinion = options.getOrNull(0)?.text ?: "",
        leftProfileName = options.getOrNull(0)?.philosopherName ?: "알 수 없음",
        leftProfileImageUrl = options.getOrNull(0)?.imageUrl ?: "",
        rightOpinion = options.getOrNull(1)?.text ?: "",
        rightProfileName = options.getOrNull(1)?.philosopherName ?: "알 수 없음",
        rightProfileImageUrl = options.getOrNull(1)?.imageUrl ?: "",
        type = when (type) {
            ContentDomainType.BATTLE -> ContentUiType.BATTLE
            ContentDomainType.VOTE -> ContentUiType.VOTE
            ContentDomainType.QUIZ -> ContentUiType.QUIZ
            else -> ContentUiType.UNKNOWN
        }
    )
}
package com.swyp4.team2.data.model

import com.swyp4.team2.domain.model.ContentDomainType
import com.swyp4.team2.domain.model.ContentOption
import com.swyp4.team2.domain.model.HomeContent
import com.swyp4.team2.domain.model.TodayPick

data class HomeResponseDto(
    val newNotice: Boolean,
    val editorPicks: List<HomeContentItemDto>,
    val trendingBattles: List<HomeContentItemDto>,
    val bestBattles: List<HomeContentItemDto>,
    val todayPicks: List<HomeContentItemDto>,
    val newBattles: List<HomeContentItemDto>
)

data class HomeContentItemDto(
    val battleId: String,
    val title: String,
    val summary: String,
    val thumbnailUrl: String,
    val type: String, // "BATTLE", "VOTE", "QUIZ"
    val viewCount: Int,
    val participantsCount: Int,
    val audioDuration: Int,
    val tags: List<String>,
    val options: List<OptionDto>
)

data class OptionDto(
    val label: String,
    val text: String,
    val imageUrl: String?
)

// 1. 일반 콘텐츠 변환 매퍼 (DTO -> Domain)
fun HomeContentItemDto.toDomainModel(): HomeContent {
    return HomeContent(
        contentId = this.battleId,
        type = runCatching { ContentDomainType.valueOf(this.type) }.getOrDefault(ContentDomainType.UNKNOWN),
        title = this.title,
        summary = this.summary,
        thumbnailUrl = this.thumbnailUrl,
        viewCount = this.viewCount,
        audioDuration = this.audioDuration,
        tags = this.tags,
        options = this.options?.map { ContentOption(it.label, it.text, it.imageUrl) } ?: emptyList()
    )
}

// 2. 오늘의 Pické 변환 매퍼 (DTO -> Domain)
fun HomeContentItemDto.toTodayPickDomainModel(): TodayPick? {
    return when (this.type) {
        "VOTE" -> TodayPick.VotePick(
            contentId = this.battleId,
            title = this.title,
            summary = this.summary,
            participantsCount = this.participantsCount,
            leftOptionText = this.options?.getOrNull(0)?.text ?: "A",
            rightOptionText = this.options?.getOrNull(1)?.text ?: "B"
        )
        "QUIZ" -> TodayPick.QuizPick(
            contentId = this.battleId,
            title = this.title,
            summary = this.summary,
            participantsCount = this.participantsCount,
            options = this.options?.map { it.text } ?: emptyList()
        )
        else -> null
    }
}
package com.swyp4.team2.data.model

import com.swyp4.team2.domain.model.ContentDomainType
import com.swyp4.team2.domain.model.ContentOption
import com.swyp4.team2.domain.model.HomeContent
import com.swyp4.team2.domain.model.TodayPick

// 최상위 응답 구조
data class HomeResponseDto(
    val newNotice: Boolean?,
    val editorPicks: List<EditorPickDto>?,
    val trendingBattles: List<TrendingBattleDto>?,
    val bestBattles: List<BestBattleDto>?,
    val todayQuizzes: List<TodayQuizDto>?,
    val todayVotes: List<TodayVoteDto>?,
    val newBattles: List<NewBattleDto>?
)

data class TagDto(
    val tagId: Long?,
    val name: String?,
    val type: String?
)

data class EditorPickDto(
    val battleId: Long?,
    val thumbnailUrl: String?,
    val optionATitle: String?,
    val optionBTitle: String?,
    val title: String?,
    val summary: String?,
    val tags: List<TagDto>?,
    val viewCount: Int?
)

data class TrendingBattleDto(
    val battleId: Long?,
    val thumbnailUrl: String?,
    val title: String?,
    val tags: List<TagDto>?,
    val audioDuration: Int?,
    val viewCount: Int?
)

data class BestBattleDto(
    val battleId: Long?,
    val philosopherA: String?,
    val philosopherB: String?,
    val title: String?,
    val tags: List<TagDto>?,
    val audioDuration: Int?,
    val viewCount: Int?
)

data class TodayQuizDto(
    val battleId: Long?,
    val title: String?,
    val summary: String?,
    val participantsCount: Int?,
    val itemA: String?,
    val itemADesc: String?,
    val itemB: String?,
    val itemBDesc: String?
)

data class TodayVoteDto(
    val battleId: Long?,
    val titlePrefix: String?,
    val titleSuffix: String?,
    val summary: String?,
    val participantsCount: Int?,
    val options: List<OptionDto>?
)

data class OptionDto(
    val label: String?,
    val title: String? // 명세서에 title로 되어 있으니 이것도 맞췄습니다!
)

data class NewBattleDto(
    val battleId: Long?,
    val thumbnailUrl: String?,
    val title: String?,
    val summary: String?,
    val philosopherA: String?,
    val philosopherAImageUrl: String?,
    val philosopherB: String?,
    val philosopherBImageUrl: String?,
    val tags: List<TagDto>?,
    val audioDuration: Int?,
    val viewCount: Int?
)

fun EditorPickDto.toDomainModel() = HomeContent(
    contentId = this.battleId?.toString() ?: "",
    type = ContentDomainType.BATTLE,
    title = this.title ?: "",
    summary = this.summary ?: "",
    thumbnailUrl = this.thumbnailUrl ?: "",
    viewCount = this.viewCount ?: 0,
    audioDuration = 0,
    tags = this.tags?.mapNotNull { it.name } ?: emptyList(),
    options = listOf(
        ContentOption("A", this.optionATitle ?: "", null),
        ContentOption("B", this.optionBTitle ?: "", null)
    )
)

fun TrendingBattleDto.toDomainModel() = HomeContent(
    contentId = this.battleId?.toString() ?: "",
    type = ContentDomainType.BATTLE,
    title = this.title ?: "",
    summary = "",
    thumbnailUrl = this.thumbnailUrl ?: "",
    viewCount = this.viewCount ?: 0,
    audioDuration = this.audioDuration ?: 0,
    tags = this.tags?.mapNotNull { it.name } ?: emptyList(),
    options = emptyList()
)

fun NewBattleDto.toDomainModel() = HomeContent(
    contentId = this.battleId?.toString() ?: "",
    type = ContentDomainType.BATTLE,
    title = this.title ?: "",
    summary = this.summary ?: "",
    thumbnailUrl = this.thumbnailUrl ?: "",
    viewCount = this.viewCount ?: 0,
    audioDuration = this.audioDuration ?: 0,
    tags = this.tags?.mapNotNull { it.name } ?: emptyList(),
    options = listOf(
        ContentOption("A", this.philosopherA ?: "", this.philosopherAImageUrl),
        ContentOption("B", this.philosopherB ?: "", this.philosopherBImageUrl)
    )
)

fun TodayQuizDto.toTodayPickDomainModel() = TodayPick.QuizPick(
    contentId = this.battleId?.toString() ?: "",
    title = this.title ?: "",
    summary = this.summary ?: "",
    participantsCount = this.participantsCount ?: 0,
    options = listOf(this.itemA ?: "", this.itemB ?: "")
)

fun TodayVoteDto.toTodayPickDomainModel() = TodayPick.VotePick(
    contentId = this.battleId?.toString() ?: "",
    title = "${this.titlePrefix ?: ""} ${this.titleSuffix ?: ""}".trim(),
    summary = this.summary ?: "",
    participantsCount = this.participantsCount ?: 0,
    leftOptionText = this.options?.getOrNull(0)?.title ?: "A",
    rightOptionText = this.options?.getOrNull(1)?.title ?: "B"
)

fun BestBattleDto.toDomainModel() = HomeContent(
    contentId = this.battleId?.toString() ?: "",
    type = ContentDomainType.BATTLE,
    title = this.title ?: "",
    summary = "",
    thumbnailUrl = "",
    viewCount = this.viewCount ?: 0,
    audioDuration = this.audioDuration ?: 0,
    tags = this.tags?.mapNotNull { it.name } ?: emptyList(),
    options = listOf(
        ContentOption("A", this.philosopherA ?: "", null),
        ContentOption("B", this.philosopherB ?: "", null)
    )
)
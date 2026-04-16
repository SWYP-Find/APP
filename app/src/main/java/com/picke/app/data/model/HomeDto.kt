package com.picke.app.data.model

import com.picke.app.domain.model.ContentDomainType
import com.picke.app.domain.model.ContentOption
import com.picke.app.domain.model.HomeContent
import com.picke.app.domain.model.PollQuizOptionStatBoard
import com.picke.app.domain.model.TodayPick

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
    val isCorrectA: Boolean?,
    val itemB: String?,
    val itemBDesc: String?,
    val isCorrectB: Boolean?
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
    val title: String?
)

data class NewBattleDto(
    val battleId: Long?,
    val thumbnailUrl: String?,
    val title: String?,
    val summary: String?,
    val optionATitle: String?,
    val philosopherA: String?,
    val philosopherAImageUrl: String?,
    val optionBTitle: String?,
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
        ContentOption(label = "A", text = this.optionATitle ?: "", philosopherName = null, imageUrl = null),
        ContentOption(label = "B", text = this.optionBTitle ?: "", philosopherName = null, imageUrl = null)
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
        ContentOption(
            label = "A",
            text = this.optionATitle ?: "",
            philosopherName = this.philosopherA ?: "",
            imageUrl = this.philosopherAImageUrl
        ),
        ContentOption(
            label = "B",
            text = this.optionBTitle ?: "",
            philosopherName = this.philosopherB ?: "",
            imageUrl = this.philosopherBImageUrl
        )
    )
)

fun TodayQuizDto.toTodayPickDomainModel() = TodayPick.QuizPick(
    contentId = this.battleId?.toString() ?: "",
    title = this.title ?: "",
    summary = this.summary ?: "",
    participantsCount = this.participantsCount ?: 0,
    selectedOptionId = null,
    options = listOf(
        PollQuizOptionStatBoard(
            optionId = 1L,
            label = "A",
            title = this.itemA ?: "",
            isCorrect = this.isCorrectA ?: false,
            voteCount = 0,
            ratio = 0f,
            stance = this.itemADesc ?: "",
        ),
        PollQuizOptionStatBoard(
            optionId = 2L,
            label = "B",
            title = this.itemB ?: "",
            isCorrect = this.isCorrectB ?: false,
            voteCount = 0,
            ratio = 0f,
            stance = this.itemBDesc ?: "",
        )
    )
)

fun TodayVoteDto.toTodayPickDomainModel() = TodayPick.VotePick(
    contentId = this.battleId?.toString() ?: "",
    // 전체 문장이 필요할 때를 대비해 합쳐줍니다.
    title = "${this.titlePrefix ?: ""} ${this.titleSuffix ?: ""}".trim(),
    titlePrefix = this.titlePrefix ?: "",
    titleSuffix = this.titleSuffix ?: "",
    summary = this.summary ?: "",
    participantsCount = this.participantsCount ?: 0,
    selectedOptionId = null,
    options = this.options?.mapIndexed { index, option ->
        PollQuizOptionStatBoard(
            optionId = (index + 1).toLong(),
            label = option.label ?: if (index == 0) "A" else "B",
            title = option.title ?: "",
            isCorrect = false,
            voteCount = 0,
            ratio = 0f,
            stance = "",
        )
    } ?: emptyList()
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
        ContentOption(label = "A", text = "", philosopherName = this.philosopherA ?: "", imageUrl = null),
        ContentOption(label = "B", text = "", philosopherName = this.philosopherB ?: "", imageUrl = null)
    )
)
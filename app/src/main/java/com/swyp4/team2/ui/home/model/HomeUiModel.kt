package com.swyp4.team2.ui.home.model

import com.swyp4.team2.domain.model.HomeBoard
import com.swyp4.team2.domain.model.HomeContent
import com.swyp4.team2.domain.model.TodayPick

enum class ContentUiType {
    BATTLE, VOTE, QUIZ, UNKNOWN
}

data class HomeBoardUiModel(
    val hasNewNotice: Boolean,
    val editorPicks: List<HomeContentUiModel>,
    val trendingBattles: List<HomeContentUiModel>,
    val bestBattles: List<HomeContentUiModel>,
    val todayPicks: List<TodayPickUiModel>,
    val newBattles: List<HomeContentUiModel>
)

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

sealed class TodayPickUiModel {
    abstract val contentId: String
    abstract val title: String
    abstract val summary: String
    abstract val participantsText: String

    data class VotePick(
        override val contentId: String,
        override val title: String,
        override val summary: String,
        override val participantsText: String,
        val leftOptionText: String,
        val rightOptionText: String
    ) : TodayPickUiModel()

    data class QuizPick(
        override val contentId: String,
        override val title: String,
        override val summary: String,
        override val participantsText: String,
        val options: List<String>
    ) : TodayPickUiModel()
}

// 1. HomeBoard 전체를 변환하는 확장 함수 추가
fun HomeBoard.toUiModel(): HomeBoardUiModel {
    return HomeBoardUiModel(
        hasNewNotice = this.hasNewNotice,
        editorPicks = this.editorPicks.map { it.toUiModel() },
        trendingBattles = this.trendingBattles.map { it.toUiModel() },
        bestBattles = this.bestBattles.map { it.toUiModel() },
        todayPicks = this.todayPicks.map { it.toUiModel() },
        newBattles = this.newBattles.map { it.toUiModel() }
    )
}

// 2. Domain의 options 리스트를 UI의 left/right 필드에 매핑하도록 수정
fun HomeContent.toUiModel(): HomeContentUiModel {
    val minutes = (this.audioDuration + 59) / 60 // 180초 -> 3분

    val leftOption = this.options.getOrNull(0)
    val rightOption = this.options.getOrNull(1)

    return HomeContentUiModel(
        contentId = this.contentId,
        type = runCatching { ContentUiType.valueOf(this.type.name) }.getOrDefault(ContentUiType.UNKNOWN),
        title = this.title,
        summary = this.summary,
        thumbnailUrl = this.thumbnailUrl,
        viewCountText = "%,d".format(this.viewCount),
        timeInfoText = "${minutes}분",
        tags = this.tags,
        leftOpinion = leftOption?.text,
        leftProfileName = leftOption?.label,
        leftProfileImageUrl = leftOption?.imageUrl,
        rightOpinion = rightOption?.text,
        rightProfileName = rightOption?.label,
        rightProfileImageUrl = rightOption?.imageUrl
    )
}

fun TodayPick.toUiModel(): TodayPickUiModel {
    val participantsText = "%,d명 참여".format(this.participantsCount)

    return when (this) {
        is TodayPick.VotePick -> TodayPickUiModel.VotePick(
            contentId = this.contentId,
            title = this.title,
            summary = this.summary,
            participantsText = participantsText,
            leftOptionText = this.leftOptionText,
            rightOptionText = this.rightOptionText
        )
        is TodayPick.QuizPick -> TodayPickUiModel.QuizPick(
            contentId = this.contentId,
            title = this.title,
            summary = this.summary,
            participantsText = participantsText,
            options = this.options
        )
    }
}
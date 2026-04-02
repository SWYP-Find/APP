package com.swyp4.team2.domain.model
enum class ContentDomainType {
    BATTLE, VOTE, QUIZ, UNKNOWN
}

data class HomeBoard(
    val hasNewNotice: Boolean,
    val editorPicks: List<HomeContent>,
    val trendingBattles: List<HomeContent>,
    val bestBattles: List<HomeContent>,
    val todayPicks: List<TodayPick>,
    val newBattles: List<HomeContent>
)

data class HomeContent(
    val contentId: String,
    val type: ContentDomainType,
    val title: String,
    val summary: String,
    val thumbnailUrl: String,
    val viewCount: Int,
    val audioDuration: Int,
    val tags: List<String>,
    val options: List<ContentOption>
)

data class ContentOption(
    val label: String,
    val text: String,
    val philosopherName: String?,
    val imageUrl: String?
)

sealed class TodayPick {
    abstract val contentId: String
    abstract val title: String
    abstract val summary: String
    abstract val participantsCount: Int

    data class VotePick(
        override val contentId: String,
        override val title: String,
        override val summary: String,
        override val participantsCount: Int,
        val leftOptionText: String,
        val rightOptionText: String
    ) : TodayPick()

    data class QuizPick(
        override val contentId: String,
        override val title: String,
        override val summary: String,
        override val participantsCount: Int,
        val options: List<String>
    ) : TodayPick()
}

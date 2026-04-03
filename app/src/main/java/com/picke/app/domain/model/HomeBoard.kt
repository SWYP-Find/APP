package com.picke.app.domain.model
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

    abstract val selectedOptionId: Long?

    abstract val options: List<PollQuizOptionStatBoard>
    abstract val type: String

    data class VotePick(
        override val contentId: String,
        override val title: String,
        val titlePrefix: String,
        val titleSuffix: String,
        override val summary: String,
        override val participantsCount: Int,
        override val selectedOptionId: Long?,
        override val options: List<PollQuizOptionStatBoard>,
        override val type: String = "POLL"
    ) : TodayPick()

    data class QuizPick(
        override val contentId: String,
        override val title: String,
        override val summary: String,
        override val participantsCount: Int,
        override val selectedOptionId: Long?,
        override val options: List<PollQuizOptionStatBoard>,
        override val type: String = "QUIZ"
    ) : TodayPick()
}

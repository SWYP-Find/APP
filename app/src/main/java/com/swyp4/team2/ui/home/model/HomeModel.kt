package com.swyp4.team2.ui.home.model

enum class ContentType {
    BATTLE, VOTE, QUIZ, UNKNOWN
}

data class HomeContentModel(
    val contentId: String,
    val type: ContentType,
    val title: String,
    val summary: String,
    val thumbnailUrl: String,
    val viewCountText: String,
    val timeInfoText: String,
    val tags: List<String>,
    val leftOpinion: String? = null,
    val leftProfileName: String? = null,
    val rightOpinion: String? = null,
    val rightProfileName: String? = null
)

sealed class TodayPickModel {
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
    ) : TodayPickModel()

    data class QuizPick(
        override val contentId: String,
        override val title: String,
        override val summary: String,
        override val participantsText: String,
        val options: List<String>
    ) : TodayPickModel()
}
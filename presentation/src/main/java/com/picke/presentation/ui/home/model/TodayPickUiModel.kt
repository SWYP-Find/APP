package com.picke.presentation.ui.home.model

import com.picke.domain.feature.home.model.TodayPick

sealed class TodayPickUiModel {
    abstract val contentId: String
    abstract val title: String
    abstract val summary: String
    abstract val participantsCount: Int
    abstract val selectedOptionId: Long?
    abstract val options: List<PollQuizOptionStatUiModel>
    abstract val type: String

    data class VotePick(
        override val contentId: String,
        override val title: String,
        val titlePrefix: String,
        val titleSuffix: String,
        override val summary: String,
        override val participantsCount: Int,
        override val selectedOptionId: Long?,
        override val options: List<PollQuizOptionStatUiModel>,
        override val type: String
    ) : TodayPickUiModel()

    data class QuizPick(
        override val contentId: String,
        override val title: String,
        override val summary: String,
        override val participantsCount: Int,
        override val selectedOptionId: Long?,
        override val options: List<PollQuizOptionStatUiModel>,
        override val type: String
    ) : TodayPickUiModel()
}

fun TodayPick.toUiModel(): TodayPickUiModel {
    return when (this) {
        is TodayPick.VotePick -> TodayPickUiModel.VotePick(
            contentId = contentId,
            title = title,
            titlePrefix = titlePrefix,
            titleSuffix = titleSuffix,
            summary = summary,
            participantsCount = participantsCount,
            selectedOptionId = selectedOptionId,
            options = options.map { it.toUiModel() },
            type = type
        )
        is TodayPick.QuizPick -> TodayPickUiModel.QuizPick(
            contentId = contentId,
            title = title,
            summary = summary,
            participantsCount = participantsCount,
            selectedOptionId = selectedOptionId,
            options = options.map { it.toUiModel() },
            type = type
        )
    }
}
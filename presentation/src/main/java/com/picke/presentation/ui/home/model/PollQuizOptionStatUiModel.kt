package com.picke.presentation.ui.home.model

import com.picke.domain.feature.pollquiz.model.PollQuizOptionStatBoard

data class PollQuizOptionStatUiModel(
    val optionId: Long,
    val title: String,
    val isCorrect: Boolean,
    val stance: String,
    val voteCount: Int,
    val ratio: Float
)

fun PollQuizOptionStatBoard.toUiModel() = PollQuizOptionStatUiModel(
    optionId = optionId,
    title = title,
    isCorrect = isCorrect,
    voteCount = voteCount,
    ratio = ratio,
    stance = stance
)
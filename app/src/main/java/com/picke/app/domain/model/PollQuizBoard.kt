package com.picke.app.domain.model

data class PollQuizVoteBoard(
    val battleId: Long,
    val selectedOptionId: Long?,
    val totalCount: Int,
    val stats: List<PollQuizOptionStatBoard>
)

data class PollQuizOptionStatBoard(
    val optionId: Long,
    val label: String,
    val title: String,
    val isCorrect: Boolean,
    val voteCount: Int,
    val ratio: Float
)


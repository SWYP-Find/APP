package com.picke.domain.feature.pollquiz.model

data class PollQuizVoteBoard(
    val battleId: Long,
    val selectedOptionId: Long?,
    val totalCount: Int,
    val stats: List<PollQuizOptionStatBoard>
)

data class PollQuizOptionStatBoard(
    val optionId: Long,
    val title: String,
    val stance: String,
    val isCorrect: Boolean,
    val voteCount: Int,
    val ratio: Float
)


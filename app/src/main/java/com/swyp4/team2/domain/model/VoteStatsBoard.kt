package com.swyp4.team2.domain.model

data class VoteStatsBoard(
    val totalCount: Int,
    val updatedAt: String,
    val options: List<VoteStatsOptionBoard>
)

data class VoteStatsOptionBoard(
    val optionId: String,
    val label: String,
    val title: String,
    val voteCount: Int,
    val ratio: Float
)
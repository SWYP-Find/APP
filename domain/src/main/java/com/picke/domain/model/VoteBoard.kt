package com.picke.domain.model

data class VoteStatsBoard(
    val totalCount: Int,
    val updatedAt: String,
    val options: List<VoteStatsOptionBoard>
)

data class VoteStatsOptionBoard(
    val optionId: Long,
    val title: String,
    val imageUrl: String,
    val isCorrect: Boolean,
    val voteCount: Int,
    val ratio: Float,
    val stance: String
)

data class MyVoteBoard(
    val battleTitle: String,
    val preVote: VotedOptionBoard?,
    val postVote: VotedOptionBoard?,
    val status: String,
    val opinionChanged: Boolean
)

data class VotedOptionBoard(
    val optionId: Long,
    val title: String
)
package com.picke.app.domain.model

import com.picke.app.ui.PhilosopherType

data class VoteBoard(
    val battleId: String,
    val bgImageUrl: String?,
    val tags: List<String>,
    val title: String,
    val preDescription: String,
    val postDescription: String,
    val optionA: VoteOption,
    val optionB: VoteOption
)

data class VoteOption(
    val optionId: String,
    val philosopherType: PhilosopherType,
    val philosopherName: String,
    val opinion: String
)

data class VoteStatsBoard(
    val totalCount: Int,
    val updatedAt: String,
    val options: List<VoteStatsOptionBoard>
)

data class VoteStatsOptionBoard(
    val optionId: Long,
    val label: String,
    val title: String,
    val voteCount: Int,
    val ratio: Float
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
    val label: String,
    val title: String
)
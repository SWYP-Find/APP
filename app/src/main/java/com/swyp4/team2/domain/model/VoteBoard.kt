package com.swyp4.team2.domain.model

import com.swyp4.team2.ui.PhilosopherType

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
package com.swyp4.team2.domain.model

data class ContentCard(
    val id: String,
    val title: String,
    val description: String,
    val optionA: String,
    val optionB: String,
    val timeLimit: String,
    val participantCount: Int,
    val hashtags: List<String>,
)

package com.swyp4.team2.domain.model

data class DiscussionHistoryBoard(
    val agreeList: List<DiscussionHistoryItem>,
    val disagreeList: List<DiscussionHistoryItem>
)

data class DiscussionHistoryItem(
    val id: String,
    val category: String,
    val title: String,
    val description: String,
    val date: String
)
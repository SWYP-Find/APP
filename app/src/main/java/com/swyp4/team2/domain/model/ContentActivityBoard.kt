package com.swyp4.team2.domain.model

data class ContentActivityItem(
    val id: Long,
    val nickname: String,
    val stance: String,
    val timeAgo: String,
    val content: String,
    val likeCount: Int
)

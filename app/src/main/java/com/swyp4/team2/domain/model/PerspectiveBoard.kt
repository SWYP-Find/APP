package com.swyp4.team2.domain.model

data class PerspectivePage(
    val items: List<PerspectiveBoard>,
    val nextCursor: String?,
    val hasNext: Boolean
)

data class PerspectiveBoard(
    val commentId: Long,
    val userTag: String,
    val nickname: String,
    val content: String,
    val isMine: Boolean,
    val createdAt: String
)
package com.swyp4.team2.domain.model

enum class PerspectiveStance(val label: String) {
    AGREE("찬성"),
    DISAGREE("반대")
}

data class PerspectivePage(
    val items: List<PerspectiveBoard>,
    val nextCursor: String?,
    val hasNext: Boolean
)

data class PerspectiveBoard(
    val commentId: Long,
    val userTag: String,
    val nickname: String,
    val characterType: String,
    val content: String,
    val isMine: Boolean,
    val createdAt: String,
    val stance: PerspectiveStance,
    val replyCount: Int,
    val likeCount: Int,
    val isLiked: Boolean
)

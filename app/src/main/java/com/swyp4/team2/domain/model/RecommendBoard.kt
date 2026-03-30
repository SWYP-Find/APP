package com.swyp4.team2.domain.model

data class RecommendUserBoard(
    val userTag: String,
    val nickname: String,
    val characterType: String,
    val characterImageUrl: String
)

// 추천 아이템 모델
data class RecommendBoard(
    val commentId: Long,
    val user: RecommendUserBoard,
    val stance: String,
    val content: String,
    val likeCount: Int,
    val isLiked: Boolean,
    val isMine: Boolean,
    val createdAt: String
)

// 추천 페이지
data class RecommendPageBoard(
    val items: List<RecommendBoard>,
    val nextCursor: String?,
    val hasNext: Boolean
)
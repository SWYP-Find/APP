package com.swyp4.team2.domain.model

data class RecommendTagBoard(
    val tagId: Long,
    val name: String
)

data class RecommendOptionBoard(
    val optionId: Long,
    val label: String,
    val title: String,
    val stance: String,
    val representative: String,
    val imageUrl: String
)

data class RecommendBoard(
    val battleId: Long,
    val title: String,
    val summary: String,
    val audioDuration: Int,
    val viewCount: Int,
    val tags: List<RecommendTagBoard>,
    val participantsCount: Int,
    val options: List<RecommendOptionBoard>
)

data class RecommendPageBoard(
    val items: List<RecommendBoard>,
    val nextCursor: String?,
    val hasNext: Boolean
)
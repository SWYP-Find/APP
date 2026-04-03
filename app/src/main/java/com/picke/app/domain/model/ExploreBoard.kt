package com.picke.app.domain.model

data class ExploreTagBoard(
    val tagId: Long,
    val name: String,
    val type: String
)

data class ExploreItemBoard(
    val battleId: Long,
    val thumbnailUrl: String,
    val type: String,
    val title: String,
    val summary: String,
    val tags: List<ExploreTagBoard>,
    val audioDuration: Int,
    val viewCount: Int
)

data class ExplorePageBoard(
    val items: List<ExploreItemBoard>,
    val nextOffset: Int?,
    val hasNext: Boolean
)
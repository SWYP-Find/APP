package com.swyp4.team2.data.model

import com.swyp4.team2.domain.model.RecommendBoard
import com.swyp4.team2.domain.model.RecommendOptionBoard
import com.swyp4.team2.domain.model.RecommendPageBoard
import com.swyp4.team2.domain.model.RecommendTagBoard

data class RecommendTagDto(
    val tagId: Long?,
    val name: String?
)

data class RecommendOptionDto(
    val optionId: Long?,
    val label: String?,
    val title: String?,
    val stance: String?,
    val representative: String?,
    val imageUrl: String?
)

data class RecommendationItemDto(
    val battleId: Long?,
    val title: String?,
    val summary: String?,
    val audioDuration: Int?,
    val viewCount: Int?,
    val tags: List<RecommendTagDto>?,
    val participantsCount: Int?,
    val options: List<RecommendOptionDto>?
)

data class RecommendationPageResponseDto(
    val items: List<RecommendationItemDto>?,
    val nextCursor: String?,
    val hasNext: Boolean?
)

fun RecommendTagDto.toDomainModel() = RecommendTagBoard(
    tagId = this.tagId ?: 0L,
    name = this.name ?: ""
)

fun RecommendOptionDto.toDomainModel() = RecommendOptionBoard(
    optionId = this.optionId ?: 0L,
    label = this.label ?: "",
    title = this.title ?: "",
    stance = this.stance ?: "",
    representative = this.representative ?: "",
    imageUrl = this.imageUrl ?: ""
)

fun RecommendationItemDto.toDomainModel() = RecommendBoard(
    battleId = this.battleId ?: 0L,
    title = this.title ?: "",
    summary = this.summary ?: "",
    audioDuration = this.audioDuration ?: 0,
    viewCount = this.viewCount ?: 0,
    tags = this.tags?.map { it.toDomainModel() } ?: emptyList(),
    participantsCount = this.participantsCount ?: 0,
    options = this.options?.map { it.toDomainModel() } ?: emptyList()
)

fun RecommendationPageResponseDto.toDomainModel() = RecommendPageBoard(
    items = this.items?.map { it.toDomainModel() } ?: emptyList(),
    nextCursor = this.nextCursor,
    hasNext = this.hasNext ?: false
)
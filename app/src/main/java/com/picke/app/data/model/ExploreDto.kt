package com.picke.app.data.model

import com.picke.app.domain.model.ExploreItemBoard
import com.picke.app.domain.model.ExplorePageBoard
import com.picke.app.domain.model.ExploreTagBoard

data class ExploreTagDto(
    val tagId: Long?,
    val name: String?,
    val type: String?
)

// [응답 DTO] 탐색/검색 아이템 단건
data class ExploreItemDto(
    val battleId: Long?,
    val thumbnailUrl: String?,
    val type: String?,
    val title: String?,
    val summary: String?,
    val tags: List<ExploreTagDto>?,
    val audioDuration: Int?,
    val viewCount: Int?
)

// [응답 DTO] 탐색 리스트 (페이지네이션)
data class ExplorePageResponseDto(
    val items: List<ExploreItemDto>?,
    val nextOffset: Int?,
    val hasNext: Boolean?
)

// DTO -> Domain
fun ExploreTagDto.toDomainModel() = ExploreTagBoard(
    tagId = this.tagId ?: 0L,
    name = this.name ?: "",
    type = this.type ?: ""
)

fun ExploreItemDto.toDomainModel() = ExploreItemBoard(
    battleId = this.battleId ?: 0L,
    thumbnailUrl = this.thumbnailUrl ?: "",
    type = this.type ?: "BATTLE",
    title = this.title ?: "",
    summary = this.summary ?: "",
    tags = this.tags?.map { it.toDomainModel() } ?: emptyList(),
    audioDuration = this.audioDuration ?: 0,
    viewCount = this.viewCount ?: 0
)

fun ExplorePageResponseDto.toDomainModel() = ExplorePageBoard(
    items = this.items?.map { it.toDomainModel() } ?: emptyList(),
    nextOffset = this.nextOffset,
    hasNext = this.hasNext ?: false
)
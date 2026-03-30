package com.swyp4.team2.data.model

import com.swyp4.team2.domain.model.TodayBattleBoard
import com.swyp4.team2.domain.model.TodayBattleItem
import com.swyp4.team2.domain.model.TodayBattleOption

data class TodayBattleResponseDto(
    val items: List<TodayBattleItemDto>,
    val totalCount: Int
)

data class TodayBattleItemDto(
    val battleId: Long,
    val title: String,
    val summary: String,
    val thumbnailUrl: String,
    val type: String,
    val audioDuration: Int,
    val tags: List<TodayBattleTagDto>,
    val options: List<TodayBattleOptionDto>
)

data class TodayBattleTagDto(
    val tagId: Long,
    val name: String,
    val type: String
)

data class TodayBattleOptionDto(
    val optionId: Long,
    val label: String,
    val title: String,
    val representative: String,
    val stance: String,
    val imageUrl: String?
)

// DTO -> Domain Model
fun TodayBattleResponseDto.toDomainModel(): TodayBattleBoard{
    return TodayBattleBoard(
        items = this.items.map { it.toDomainModel() },
        totalCount = this.totalCount
    )
}

fun TodayBattleItemDto.toDomainModel(): TodayBattleItem {
    return TodayBattleItem(
        battleId = this.battleId.toString(),
        title = this.title,
        summary = this.summary,
        thumbnailUrl = this.thumbnailUrl ?: "",
        type = this.type,
        audioDuration = this.audioDuration,
        tags = this.tags.map { it.name },
        options = this.options.map { it.toDomainModel() }
    )
}

fun TodayBattleOptionDto.toDomainModel(): TodayBattleOption {
    return TodayBattleOption(
        optionId = this.optionId.toString(),
        label = this.label,
        title = this.title,
        representative = this.representative ?: "",
        stance = this.stance,
        imageUrl = this.imageUrl
    )
}
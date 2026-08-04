package com.picke.data.feature.todaybattle.model

import com.picke.domain.model.TodayBattleBoard
import com.picke.domain.model.TodayBattleItem
import com.picke.domain.model.TodayBattleOption

data class TodayBattleResponseDto(
    val items: List<TodayBattleItemDto>,
    val totalCount: Int
)

data class TodayBattleItemDto(
    val battleId: Long,
    val title: String,
    val summary: String,
    val thumbnailUrl: String,
    val viewCount: Int,
    val participantsCount: Long,
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
        type = "",
        viewCount = this.viewCount,
        participantsCount = this.participantsCount,
        audioDuration = this.audioDuration,
        tags = this.tags.map { it.name },
        options = this.options.map { it.toDomainModel() }
    )
}

fun TodayBattleOptionDto.toDomainModel(): TodayBattleOption {
    return TodayBattleOption(
        optionId = this.optionId.toString(),
        title = this.title,
        representative = this.representative ?: "",
        stance = this.stance,
        imageUrl = this.imageUrl
    )
}
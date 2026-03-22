package com.swyp4.team2.data.model

import com.swyp4.team2.domain.model.TodayBattleBoard
import com.swyp4.team2.domain.model.TodayBattleItem
import com.swyp4.team2.domain.model.TodayBattleOption

data class TodayBattleResponseDto(
    val items: List<TodayBattleItemDto>,
    val totalCount: Int
)

data class TodayBattleItemDto(
    val battleId: String,
    val title: String,
    val summary: String,
    val thumbnailUrl: String,
    val type: String,
    val audioDuration: Int,
    val tags: List<TodayBattleTagDto>,
    val options: List<TodayBattleOptionDto>
)

data class TodayBattleTagDto(
    val tagId: String,
    val name: String,
    val type: String
)

data class TodayBattleOptionDto(
    val optionId: String,
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
        battleId = this.battleId,
        title = this.title,
        summary = this.summary,
        thumbnailUrl = this.thumbnailUrl,
        type = this.type,
        audioDuration = this.audioDuration,
        tags = this.tags.map { it.name }, // 🌟 태그 객체에서 '이름'만 쏙 뽑아서 List<String>으로!
        options = this.options.map { it.toDomainModel() }
    )
}

fun TodayBattleOptionDto.toDomainModel(): TodayBattleOption {
    return TodayBattleOption(
        optionId = this.optionId,
        label = this.label,
        title = this.title,
        representative = this.representative,
        stance = this.stance,
        imageUrl = this.imageUrl
    )
}
package com.picke.app.domain.model

data class TodayBattleBoard(
    val items: List<TodayBattleItem>,
    val totalCount: Int
)

data class TodayBattleItem(
    val battleId: String,
    val title: String,
    val summary: String,
    val thumbnailUrl: String,
    val type: String,
    val viewCount: Int,
    val participantsCount: Long,
    val audioDuration: Int,
    val tags: List<String>,
    val options: List<TodayBattleOption>
)

data class TodayBattleOption(
    val optionId: String,
    val label: String,
    val title: String,
    val representative: String,
    val stance: String,
    val imageUrl: String?
)
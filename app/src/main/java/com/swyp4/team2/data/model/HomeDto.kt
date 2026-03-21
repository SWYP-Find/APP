package com.swyp4.team2.data.model

data class HomeResponseDto(
    val newNotice: Boolean,
    val editorPicks: List<HomeContentItemDto>,
    val trendingBattles: List<HomeContentItemDto>,
    val bestBattles: List<HomeContentItemDto>,
    val todayPicks: List<HomeContentItemDto>,
    val newBattles: List<HomeContentItemDto>
)

data class HomeContentItemDto(
    val battleId: String,
    val title: String,
    val summary: String,
    val thumbnailUrl: String,
    val type: String, // "BATTLE", "VOTE", "QUIZ"
    val viewCount: Int,
    val participantsCount: Int,
    val audioDuration: Int,
    val tags: List<String>,
    val options: List<OptionDto>
)

data class OptionDto(
    val label: String,
    val text: String,
    val image: String? = null
)
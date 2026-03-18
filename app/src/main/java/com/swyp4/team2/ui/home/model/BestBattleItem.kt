package com.swyp4.team2.ui.home.model

data class BestBattleItem(
    val id: Int,
    val rank: Int,
    val vsBadge: String,
    val title: String,
    val tags: List<String>,
    val timeAgo: String,
    val viewCount: String
)

val dummyBestBattleList = listOf(
    BestBattleItem(1, 1, "맹자 VS 순자", "인간은 본래 선한가, 악한가?", listOf("철학", "인문"), "8분", "1,340"),
    BestBattleItem(2, 2, "칸트 VS 톨스토이", "죽음을 앞둔 사람에게 진실을 말해야 하는가?", listOf("철학", "인문"), "8분", "1,340"),
    BestBattleItem(3, 3, "튜링 VS 설", "AI와 사랑에 빠지는 것, 진짜 사랑인가?", listOf("철학", "인문"), "8분", "1,340")
)

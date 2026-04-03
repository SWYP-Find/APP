package com.picke.app.ui.home.model

data class TrendingBattleItem(
    val id: Int,
    val imageUrl: String,
    val category: String,
    val title: String,
    val timeAgo: String,
    val viewCount: String
)

// 테스트용 더미 데이터
val dummyTrendingList = listOf(
    TrendingBattleItem(1, "https://picsum.photos/200/150", "철학", "인간은 본래 선한가, 악한가?", "8분", "1,340"),
    TrendingBattleItem(2, "https://picsum.photos/201/150", "역사", "안락사 도입, 당신의 선택은?", "5분", "1,132"),
    TrendingBattleItem(3, "https://picsum.photos/202/150", "과학", "AI는 의식을 가질 수 있는가?", "12분", "890")
)
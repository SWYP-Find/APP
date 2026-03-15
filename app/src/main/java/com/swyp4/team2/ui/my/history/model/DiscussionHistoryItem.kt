package com.swyp4.team2.ui.my.history.model

data class DiscussionHistoryItem(
    val id: Int,
    val title: String,
    val category: String,
    val timeAgo: String,
    val stance: String
)

val dummyAgreeList = listOf(
    DiscussionHistoryItem(1, "안락사 도입, 찬성 vs 반대", "철학", "1달 전", "찬성"),
    DiscussionHistoryItem(2, "의무와 행복이 충돌할 때", "철학", "2달 전", "찬성"),
    DiscussionHistoryItem(3, "인간은 수단이 될 수 있는가?", "철학", "2달 전", "찬성")
)

val dummyDisagreeList = listOf(
    DiscussionHistoryItem(4, "선의의 거짓말은 정당한가?", "도덕", "1달 전", "반대"),
    DiscussionHistoryItem(5, "동물권 보장, 어디까지?", "사회", "3달 전", "반대")
)

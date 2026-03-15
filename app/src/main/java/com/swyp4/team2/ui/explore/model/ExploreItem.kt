package com.swyp4.team2.ui.explore.model

data class ExploreItem(
    val id: Int,
    val type: String, // "배틀" 등
    val title: String,
    val description: String,
    val timeAgo: String,
    val viewCount: String,
    val imageUrl: String // 실제로는 서버 URL이 들어갑니다.
)

// 🌟 테스트용 더미 데이터
val dummyExploreList = List(10) {
    ExploreItem(
        id = it,
        type = "배틀",
        title = "아틀란티스는 실존하는가?",
        description = "아리스토텔레스가 증언함에도 불구하고 아틀란티스가 허구라고 단언할 수 있는가?",
        timeAgo = "8분",
        viewCount = "1,340",
        imageUrl = "https://picsum.photos/seed/${it}/200/200" // 랜덤 이미지
    )
}

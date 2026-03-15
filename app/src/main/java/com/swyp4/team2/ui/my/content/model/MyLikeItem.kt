package com.swyp4.team2.ui.my.content.model

data class MyLikeItem(
    val id: Int,
    val profileImgRes: Int? = null, // 임시 프로필 이미지용
    val nickname: String,
    val stance: String, // 찬성/반대
    val timeAgo: String,
    val content: String,
    val likeCount: String
)

val dummyLikeList = List(10) {
    MyLikeItem(
        id = it,
        nickname = "사유하는 쿼카",
        stance = "찬성",
        timeAgo = "2분 전",
        content = "제도화가 무서운 건, 사회적 압력이 '선택'을 '의무'로 바꿀 수 있다는 거예요. 네덜란드 사례를 보면 우려가 현실이 되고 있죠.",
        likeCount = "1,340"
    )
}

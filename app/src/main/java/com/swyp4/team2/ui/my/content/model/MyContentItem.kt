package com.swyp4.team2.ui.my.content.model

// 🔥 하나의 공통 모델로 통합
data class ContentActivityItem(
    val id: Int,
    val profileImgRes: Int? = null,
    val nickname: String,
    val stance: String, // "찬성" 또는 "반대"
    val timeAgo: String,
    val content: String,
    val likeCount: String
)

// 내 댓글 더미 데이터
val dummyCommentList = listOf(
    ContentActivityItem(
        id = 0,
        nickname = "사색하는 고양이",
        stance = "반대",
        timeAgo = "2분 전",
        content = "제도화가 무서운 건, 사회적 압력이 '선택'을 '의무'로 바꿀 수 있다는 거예요. 네덜란드 사례를 보면 우려가 현실이 되고 있죠. 제도화가 무서운 건, 사회적 압력이 '선택'을 '의무'로 바꿀 수...",
        likeCount = "1,340"
    ),
    ContentActivityItem(
        id = 1,
        nickname = "사색하는 고양이",
        stance = "찬성",
        timeAgo = "2분 전",
        content = "제도화가 무서운 건, 사회적 압력이 '선택'을 '의무'로 바꿀 수 있다는 거예요. 네덜란드 사례를 보면 우려가 현실이 되고 있죠. 제도화가 무서운 건, 사회적 압력이 '선택'을 '의무'로 바꿀 수...",
        likeCount = "1,340"
    )
)

// 좋아요 더미 데이터
val dummyLikeList = listOf(
    ContentActivityItem(
        id = 10,
        nickname = "사색하는 고슴도치",
        stance = "반대",
        timeAgo = "2분 전",
        content = "제도화가 무서운 건, 사회적 압력이 '선택'을 '의무'로 바꿀 수 있다는 거예요. 네덜란드 사례를 보면 우려가 현실이 되고 있죠. 제도화가 무서운 건, 사회적 압력이 '선택'을 '의무'로 바꿀 수...",
        likeCount = "1,340"
    ),
    ContentActivityItem(
        id = 11,
        nickname = "사색하는 호랑이",
        stance = "찬성",
        timeAgo = "2분 전",
        content = "제도화가 무서운 건, 사회적 압력이 '선택'을 '의무'로 바꿀 수 있다는 거예요. 네덜란드 사례를 보면 우려가 현실이 되고 있죠. 제도화가 무서운 건, 사회적 압력이 '선택'을 '의무'로 바꿀 수...",
        likeCount = "1,340"
    )
)

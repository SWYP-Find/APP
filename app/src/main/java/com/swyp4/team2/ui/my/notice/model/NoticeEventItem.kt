package com.swyp4.team2.ui.my.notice.model

data class NoticeEventItem(
    val id: Int,
    val type: String, // "공지사항" 또는 "이벤트"
    val title: String,
    val date: String
)

val dummyNoticeList = List(5) {
    NoticeEventItem(
        id = it,
        type = "공지사항",
        title = "공지사항입니다. 공지사항입니다.공지사항입니다.공지...",
        date = "2026-03-01"
    )
}

val dummyEventList = List(5) {
    NoticeEventItem(
        id = it + 10,
        type = "이벤트",
        title = "이벤트 페이지입니다.이벤트 페이지입니다.이벤트 페이...",
        date = "2026-03-01"
    )
}

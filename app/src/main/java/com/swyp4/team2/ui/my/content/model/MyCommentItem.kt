package com.swyp4.team2.ui.my.content.model

data class MyCommentItem(
    val id: Int,
    val category: String,
    val title: String,
    val content: String,
    val date: String
)

val dummyCommentList = List(10) {
    MyCommentItem(
        id = it,
        category = "철학",
        title = "안락사 도입, 찬성 vs 반대",
        content = "전쟁 상황이 지가 바라는대로 이루어 진다고 생각하는걸까? 세상사람들이 다 지말대로 움직일거라는 저 근자감이 세계를 망치고 있는줄도 모르는 정말 멍충이..",
        date = "2026.03.11"
    )
}
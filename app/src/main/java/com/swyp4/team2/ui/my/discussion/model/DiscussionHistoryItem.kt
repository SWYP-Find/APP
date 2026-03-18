package com.swyp4.team2.ui.my.discussion.model

data class DiscussionHistoryItem(
    val id: Int,
    val title: String,
    val category: String,
    val date: String,
    val description: String
)

val dummyAgreeList = listOf(
    DiscussionHistoryItem(
        id = 1,
        title = "안락사 도입, 찬성 vs 반대",
        category = "철학",
        date = "2026.03.11",
        description = "전쟁 상황이 지가 바라는대로 이루어 진다고 생각하는걸까? 세상사람들이 다 지말대로 움직일거라는 저 근자감이 세계를 망치고 있는줄도 모르는 정말 멍청이.."
    ),
    DiscussionHistoryItem(
        id = 2,
        title = "안락사 도입, 찬성 vs 반대",
        category = "철학",
        date = "2026.03.11",
        description = "전쟁 상황이 지가 바라는대로 이루어 진다고 생각하는걸까? 세상사람들이 다 지말대로 움직일거라는 저 근자감이 세계를 망치고 있는줄도 모르는 정말 멍청이.."
    ),
    DiscussionHistoryItem(
        id = 3,
        title = "안락사 도입, 찬성 vs 반대",
        category = "철학",
        date = "2026.03.11",
        description = "전쟁 상황이 지가 바라는대로 이루어 진다고 생각하는걸까? 세상사람들이 다 지말대로 움직일거라는 저 근자감이 세계를 망치고 있는줄도 모르는 정말 멍청이.."
    )
)

val dummyDisagreeList = listOf(
    DiscussionHistoryItem(
        id = 4,
        title = "선의의 거짓말은 정당한가?",
        category = "도덕",
        date = "2026.03.10",
        description = "거짓말은 결국 신뢰를 무너뜨립니다. 단기적인 평화보다 장기적인 신뢰가 더 중요하다고 생각합니다."
    ),
    DiscussionHistoryItem(
        id = 5,
        title = "동물권 보장, 어디까지?",
        category = "사회",
        date = "2026.02.25",
        description = "모든 생명이 존중받아야 하지만, 인간의 생존과 직결된 연구 등에서는 어느 정도의 타협이 불가피한 현실입니다."
    )
)

package com.swyp4.team2.ui.vote.model

import com.swyp4.team2.R
import com.swyp4.team2.ui.home.model.BattleProfile

enum class VoteType {
    PRE,  // 사전 투표 (밝은 테마)
    POST  // 사후 투표 (어두운 테마)
}

// 투표 화면에 필요한 데이터
data class VoteTopicItem(
    val id: Int,
    val bgImageRes: Int,
    val tags: List<String>,
    val title: String,
    val preDescription: String,  // 사전 투표 설명
    val postDescription: String, // 사후 투표 설명
    val leftProfile: BattleProfile,
    val rightProfile: BattleProfile
)

val dummyVoteItem = VoteTopicItem(
    id = 1,
    bgImageRes = R.drawable.bg_rose,
    tags = listOf("예술", "현대미술"),
    title = "뒤샹의 변기,\n예술인가 도발인가",
    preDescription = "누군가는 이것을 화장실의 부속품이라 부르고,\n누군가는 현대 미술의 혁명이라고 부릅니다.\n과연 이 변기의 '진짜 모습'은 무엇일까요?",
    postDescription = "생각이 정리되셨다면, 최종 입장을 선택해주세요.",
    leftProfile = BattleProfile(
        profileImg = R.drawable.ic_launcher_foreground,
        opinion = "변기는 변기다",
        name = "플라톤"
    ),
    rightProfile = BattleProfile(
        profileImg = R.drawable.ic_launcher_foreground,
        opinion = "예술이다",
        name = "사르트르"
    )
)
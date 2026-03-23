package com.swyp4.team2.ui.vote.model

import com.swyp4.team2.ui.PhilosopherType

enum class VoteType {
    PRE,  // 사전 투표 (밝은 테마)
    POST  // 사후 투표 (어두운 테마)
}

// 투표 화면에 필요한 데이터
data class VoteUiModel(
    val battleId: String, // API 통신 시 필요함
    val bgImageUrl: String?, // 🌟 백엔드에서 주는 이미지 URL (String)
    val tags: List<String>,
    val title: String,
    val preDescription: String,
    val postDescription: String,
    val optionA: VoteOptionUiModel, // 왼쪽 (A)
    val optionB: VoteOptionUiModel
)

data class VoteOptionUiModel(
    val optionId: String, // "A" 또는 "B"
    val philosopherType: PhilosopherType,
    val philosopherName: String,
    val opinion: String
)

// UI 테스트를 위한 더미 데이터
val dummyVoteItem = VoteUiModel(
    battleId = "dummy-battle-id",
    bgImageUrl = "https://example.com/dummy.jpg", // 임시 URL
    tags = listOf("예술", "현대미술"),
    title = "뒤샹의 변기,\n예술인가 도발인가",
    preDescription = "누군가는 이것을 화장실의 부속품이라 부르고,\n누군가는 현대 미술의 혁명이라고 부릅니다.\n과연 이 변기의 '진짜 모습'은 무엇일까요?",
    postDescription = "생각이 정리되셨다면, 최종 입장을 선택해주세요.",
    optionA = VoteOptionUiModel(
        optionId = "A",
        philosopherType = PhilosopherType.PLATO,
        philosopherName = "플라톤",
        opinion = "변기는 변기다"
    ),
    optionB = VoteOptionUiModel(
        optionId = "B",
        philosopherType = PhilosopherType.UNKNOWN, // 사르트르가 없다면 UNKNOWN 등으로 처리
        philosopherName = "사르트르",
        opinion = "예술이다"
    )
)
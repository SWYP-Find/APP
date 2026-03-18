package com.swyp4.team2.ui.home.model

import com.swyp4.team2.R

data class BattleProfile(
    val profileImg: Int, // SVG 리소스 ID
    val opinion: String, // "선하다"
    val name: String     // "맹자"
)

data class NewBattleItem(
    val id: Int,
    val category: String,
    val title: String,
    val description: String,
    val timeAgo: String,
    val viewCount: String,
    val leftProfile: BattleProfile,
    val rightProfile: BattleProfile
)

// 더미 데이터 (첫 번째 카드 예시)
val dummyNewBattleList = listOf(
    NewBattleItem(
        id = 1,
        category = "철학",
        title = "인간은 본래 선한가, 악한가?",
        description = "인간 본성의 선악과 문명의 역할에 관한 철학적 대결!",
        timeAgo = "5분",
        viewCount = "726",
        leftProfile = BattleProfile(R.drawable.ic_profile_mengzi, "선하다", "맹자"),
        rightProfile = BattleProfile(R.drawable.ic_profile_xunzi, "악하다", "순자")
    ),
    NewBattleItem(
        id = 1,
        category = "철학",
        title = "인간은 본래 선한가, 악한가?",
        description = "인간 본성의 선악과 문명의 역할에 관한 철학적 대결!",
        timeAgo = "5분",
        viewCount = "726",
        leftProfile = BattleProfile(R.drawable.ic_profile_mengzi, "선하다", "맹자"),
        rightProfile = BattleProfile(R.drawable.ic_profile_xunzi, "악하다", "순자")
    ),
    NewBattleItem(
        id = 1,
        category = "철학",
        title = "인간은 본래 선한가, 악한가?",
        description = "인간 본성의 선악과 문명의 역할에 관한 철학적 대결!",
        timeAgo = "5분",
        viewCount = "726",
        leftProfile = BattleProfile(R.drawable.ic_profile_mengzi, "선하다", "맹자"),
        rightProfile = BattleProfile(R.drawable.ic_profile_xunzi, "악하다", "순자")
    )
)

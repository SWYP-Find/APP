package com.swyp4.team2.ui.curation.model

import com.swyp4.team2.ui.home.model.BattleProfile

data class CurationBattleItem(
    val id: Int,
    val tag: String,
    val timeLimit: String, // 예: "5분"
    val viewCount: Int,    // 예: 726
    val title: String,
    val description: String,
    val leftProfile: BattleProfile,
    val rightProfile: BattleProfile
)

package com.swyp4.team2.domain.model

import com.swyp4.team2.ui.home.model.HomeContentModel
import com.swyp4.team2.ui.home.model.TodayPickModel

data class HomeBoardData(
    val hasNewNotice: Boolean,
    val editorPicks: List<HomeContentModel>,
    val trendingBattles: List<HomeContentModel>,
    val bestBattles: List<HomeContentModel>,
    val todayPicks: List<TodayPickModel>,
    val newBattles: List<HomeContentModel>
)

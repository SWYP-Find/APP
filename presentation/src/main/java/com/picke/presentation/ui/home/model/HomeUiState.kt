package com.picke.presentation.ui.home.model

import com.picke.presentation.ui.attendance.AttendanceCheckUiState

data class HomeUiState(
    val isLoading: Boolean = false,
    val hasNewNotice: Boolean = false,
    val isAlarmStatusLoading: Boolean = true,
    val editorPicks: List<HomeContentUiModel> = emptyList(),
    val trendingBattles: List<HomeContentUiModel> = emptyList(),
    val bestBattles: List<HomeContentUiModel> = emptyList(),
    val newBattles: List<HomeContentUiModel> = emptyList(),
    val todayPicks: List<TodayPickUiModel> = emptyList(),
    val attendanceCheckUiState: AttendanceCheckUiState? = null
)
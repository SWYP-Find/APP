package com.picke.presentation.util

import kotlinx.coroutines.flow.MutableSharedFlow

sealed class DeepLinkEvent {
    data class GoToBattle(val battleId: String) : DeepLinkEvent()
    data class GoToTodayBattle(val battleId: String) : DeepLinkEvent()
    data class GoToReport(val reportId: String) : DeepLinkEvent()
    data object GoToAlarm : DeepLinkEvent()
    data class GoToPerspective(val perspectiveId: String, val commentId: String?) : DeepLinkEvent()
}

object DeepLinkManager {
    var pendingReportId: String? = null
    var pendingBattleId: String? = null
    // MainScreen 진입 시 홈 탭 대신 열어야 할 탭의 route (예: 탐색 탭). 소비 후 즉시 null로 리셋한다.
    var pendingTab: String? = null
    val deepLinkEvent = MutableSharedFlow<DeepLinkEvent>(replay = 1, extraBufferCapacity = 1)
}
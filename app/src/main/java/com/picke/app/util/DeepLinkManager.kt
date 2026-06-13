package com.picke.app.util

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
    val deepLinkEvent = MutableSharedFlow<DeepLinkEvent>(extraBufferCapacity = 1)
}
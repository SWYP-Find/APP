package com.picke.app.util

import kotlinx.coroutines.flow.MutableSharedFlow

sealed class DeepLinkEvent {
    data class GoToBattle(val battleId: String) : DeepLinkEvent()
    data class GoToReport(val reportId: String) : DeepLinkEvent()
}

object DeepLinkManager {
    // 철학자 리포트 ID를 저장할 공간
    var pendingReportId: String? = null

    // 배틀 ID를 저장할 공간
    var pendingBattleId: String? = null
    val deepLinkEvent = MutableSharedFlow<DeepLinkEvent>(extraBufferCapacity = 1)
}
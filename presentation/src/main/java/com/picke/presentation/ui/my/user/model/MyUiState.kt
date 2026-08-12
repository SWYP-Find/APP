package com.picke.presentation.ui.my.user.model

data class MyUiState(
    val profile: MyProfileUiModel? = null,
    val philosopher: MyPhilosopherUiModel? = null,
    val tier: MyTierUiModel? = null,
    val hasNewNotice: Boolean = false,
    val isLoading: Boolean = false,
    // 미읽음 알림 여부 조회가 끝나기 전까지 탑바 아이콘을 shimmer로 보여주기 위한 플래그
    val isAlarmStatusLoading: Boolean = true
)
package com.picke.presentation.ui.my.user.model

data class MyUiState(
    val profile: MyProfileUiModel? = null,
    val philosopher: MyPhilosopherUiModel? = null,
    val tier: MyTierUiModel? = null,
    val hasNewNotice: Boolean = false,
    val isLoading: Boolean = false,
    val isAlarmStatusLoading: Boolean = true
)
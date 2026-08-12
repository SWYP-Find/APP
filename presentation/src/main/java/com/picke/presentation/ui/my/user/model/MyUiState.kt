package com.picke.presentation.ui.my.user.model

import com.picke.domain.feature.mypage.model.MyPhilosopher
import com.picke.domain.feature.mypage.model.MyProfile
import com.picke.domain.feature.mypage.model.MyTier

data class MyUiState(
    val profile: MyProfile? = null,
    val philosopher: MyPhilosopher? = null,
    val tier: MyTier? = null,
    val hasNewNotice: Boolean = false,
    val isLoading: Boolean = false,
    // 미읽음 알림 여부 조회가 끝나기 전까지 탑바 아이콘을 shimmer로 보여주기 위한 플래그
    val isAlarmStatusLoading: Boolean = true
)
package com.picke.presentation.ui.vote.model

data class VoteUiState(
    val isLoading: Boolean = false, // 로딩 상태
    val battleDetail: BattleDetailUiModel? = null, // 배틀 상세 정보
    val error: String? = null, // 에러 메시지
    val isInsufficientPoints: Boolean = false // 포인트 부족 여부 (다이얼로그 트리거)
)
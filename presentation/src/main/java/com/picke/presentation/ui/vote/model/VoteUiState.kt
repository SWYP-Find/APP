package com.picke.presentation.ui.vote.model

data class VoteUiState(
    val isLoading: Boolean = false,
    val battleDetail: BattleDetailUiModel? = null,
    val error: String? = null,
    val isInsufficientPoints: Boolean = false
)
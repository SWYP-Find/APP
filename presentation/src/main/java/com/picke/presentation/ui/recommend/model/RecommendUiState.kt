package com.picke.presentation.ui.recommend.model

data class RecommendUiState(
    val battleId: String = "",
    val recommendList: List<RecommendUiModel> = emptyList(),
    val isLoading: Boolean = false
)
package com.picke.presentation.ui.recommend.model

import com.picke.presentation.ui.recommend.RecommendUiModel

data class RecommendUiState(
    val battleId: String = "",
    val recommendList: List<RecommendUiModel> = emptyList(),
    val isLoading: Boolean = false
)
package com.picke.presentation.ui.todaybattle.model

data class TodayBattleUiState(
    val isLoading: Boolean = true,
    val isEntering: Boolean = false,
    val battleList: List<TodayBattleUiModel> = emptyList(),
    val errorMessage: String? = null
)
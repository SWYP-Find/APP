package com.picke.presentation.ui.alarm.model

import com.picke.domain.feature.alarm.model.AlarmItemBoard

data class AlarmUiState(
    val alarmList: List<AlarmItemBoard> = emptyList(),
    val selectedCategory: String = "ALL",
    val isLoading: Boolean = false,
    val page: Int = 0,
    val hasNext: Boolean = true,
    val isPagingLoading: Boolean = false
)
package com.picke.presentation.ui.alarm.model

sealed class AlarmUiEvent {
    data class ShowToast(val message: String) : AlarmUiEvent()
}
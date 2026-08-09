package com.picke.presentation.ui.perspective.model

sealed class PerspectiveUiEvent {
    data class ShowToast(val message: String) : PerspectiveUiEvent()
}
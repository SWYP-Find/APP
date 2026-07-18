package com.picke.app.ui.my.setting.alarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.app.domain.model.NotificationSettingsBoard
import com.picke.app.domain.usecase.mypage.GetNotificationSettingsUseCase
import com.picke.app.domain.usecase.mypage.UpdateNotificationSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingAlarmUiState(
    val isLoading: Boolean = false,
    val settings: NotificationSettingsBoard? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class SettingAlarmViewModel @Inject constructor(
    private val getNotificationSettingsUseCase: GetNotificationSettingsUseCase,
    private val updateNotificationSettingsUseCase: UpdateNotificationSettingsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingAlarmUiState())
    val uiState: StateFlow<SettingAlarmUiState> = _uiState.asStateFlow()

    init {
        loadNotificationSettings()
    }

    private fun loadNotificationSettings() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            getNotificationSettingsUseCase()
                .onSuccess { settings ->
                    _uiState.update { it.copy(isLoading = false, settings = settings) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
        }
    }

    fun updateSetting(updated: NotificationSettingsBoard) {
        val previous = _uiState.value.settings ?: return
        // 낙관적 업데이트: 즉시 UI 반영 후 API 실패 시 롤백
        _uiState.update { it.copy(settings = updated) }
        viewModelScope.launch {
            updateNotificationSettingsUseCase(updated)
                .onFailure {
                    _uiState.update { it.copy(settings = previous) }
                }
        }
    }
}

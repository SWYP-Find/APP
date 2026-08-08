package com.picke.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.common.local.LocalPreferencesUseCases
import com.picke.domain.feature.alarm.usecase.AlarmUseCases
import com.picke.domain.feature.attendance.usecase.AttendanceUseCases
import com.picke.domain.feature.home.usecase.HomeUseCases
import com.picke.presentation.ui.attendance.toAttendanceCheckUiState
import com.picke.presentation.ui.home.model.HomeUiState
import com.picke.presentation.ui.home.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCases: HomeUseCases,
    private val attendanceUseCases: AttendanceUseCases,
    private val alarmUseCases: AlarmUseCases,
    private val localPreferencesUseCases: LocalPreferencesUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var isAttendanceFlowTriggered = false

    init {
        fetchHomeData()
    }

    fun checkInAndShowAttendanceSheetIfNeeded() {
        val today = LocalDate.now().toString()
        if (isAttendanceFlowTriggered || localPreferencesUseCases.getLastAttendanceSheetShownDate() == today) return

        isAttendanceFlowTriggered = true

        viewModelScope.launch {
            attendanceUseCases.checkAttendanceUseCase()
            attendanceUseCases.getWeeklyAttendanceUseCase()
                .onSuccess { weeklyAttendance ->
                    localPreferencesUseCases.saveLastAttendanceSheetShownDate(today)
                    _uiState.update {
                        it.copy(attendanceCheckUiState = weeklyAttendance.toAttendanceCheckUiState())
                    }
                }
        }
    }

    fun dismissAttendanceCheckSheet() {
        _uiState.update { it.copy(attendanceCheckUiState = null) }
    }

    fun fetchHomeData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            homeUseCases.fetchHomeDataUseCase()
                .onSuccess { boardData ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            editorPicks = boardData.editorPicks.map { it.toUiModel() },
                            trendingBattles = boardData.trendingBattles.map { it.toUiModel() },
                            bestBattles = boardData.bestBattles.map { it.toUiModel() },
                            newBattles = boardData.newBattles.map { it.toUiModel() },
                            todayPicks = emptyList()
                        )
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }

    fun fetchUnreadAlarmStatus() {
        _uiState.update { it.copy(isAlarmStatusLoading = true) }

        viewModelScope.launch {
            alarmUseCases.getUnreadAlarmStatusUseCase()
                .onSuccess { hasUnread ->
                    _uiState.update {
                        it.copy(
                            hasNewNotice = hasUnread,
                            isAlarmStatusLoading = false
                        )
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(isAlarmStatusLoading = false) }
                }
        }
    }
}
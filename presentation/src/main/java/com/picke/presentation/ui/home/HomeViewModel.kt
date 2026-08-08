package com.picke.presentation.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.common.local.LocalPreferencesUseCases
import com.picke.domain.feature.alarm.usecase.AlarmUseCases
import com.picke.domain.feature.attendance.usecase.AttendanceUseCases
import com.picke.domain.feature.home.usecase.HomeUseCases
import com.picke.domain.feature.pollquiz.usecase.PollQuizUseCases
import com.picke.presentation.ui.attendance.AttendanceCheckUiState
import com.picke.presentation.ui.attendance.toAttendanceCheckUiState
import com.picke.ui.home.model.HomeContentUiModel
import com.picke.ui.home.model.TodayPickUiModel
import com.picke.ui.home.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = true,
    val hasNewNotice: Boolean = false,
    // 미읽음 알림 여부 조회가 끝나기 전까지 탑바 아이콘을 shimmer로 보여주기 위한 플래그
    val isAlarmStatusLoading: Boolean = true,
    val editorPicks: List<HomeContentUiModel> = emptyList(),
    val trendingBattles: List<HomeContentUiModel> = emptyList(),
    val bestBattles: List<HomeContentUiModel> = emptyList(),
    val newBattles: List<HomeContentUiModel> = emptyList(),
    val todayPicks: List<TodayPickUiModel> = emptyList(),
    // null이 아니면 당일 최초 진입 출석체크 바텀시트를 노출한다.
    val attendanceCheckUiState: AttendanceCheckUiState? = null
)

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
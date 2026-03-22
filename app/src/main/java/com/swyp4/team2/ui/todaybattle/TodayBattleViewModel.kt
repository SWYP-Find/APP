package com.swyp4.team2.ui.todaybattle

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swyp4.team2.domain.repository.TodayBattleRepository
import com.swyp4.team2.ui.todaybattle.model.TodayBattleUiModel
import com.swyp4.team2.ui.todaybattle.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// 🌟 화면 전체의 기분(상태)
data class TodayBattleUiState(
    val isLoading: Boolean = true,
    val battleList: List<TodayBattleUiModel> = emptyList(), // 우리가 그릴 배틀 리스트!
    val errorMessage: String? = null
)

@HiltViewModel
class TodayBattleViewModel @Inject constructor(
    private val todayBattleRepository: TodayBattleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodayBattleUiState())
    val uiState: StateFlow<TodayBattleUiState> = _uiState.asStateFlow()

    init {
        fetchTodayBattles()
    }

    private fun fetchTodayBattles() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            todayBattleRepository.fetchTodayBattles()
                .onSuccess { board ->
                    Log.d("BattleFlow", "🟢 배틀 목록 불러오기 성공! (${board.items.size}개)")
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            battleList = board.items.map { it.toUiModel() }
                        )
                    }
                }
                .onFailure { error ->
                    Log.e("BattleFlow", "🔴 배틀 목록 불러오기 실패!", error)
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = error.message)
                    }
                }
        }
    }
}
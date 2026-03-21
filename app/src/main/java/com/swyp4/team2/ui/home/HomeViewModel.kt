package com.swyp4.team2.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swyp4.team2.domain.repository.HomeRepository
import com.swyp4.team2.ui.home.model.HomeContentModel
import com.swyp4.team2.ui.home.model.TodayPickModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = true,
    val hasNewNotice: Boolean = false,
    val editorPicks: List<HomeContentModel> = emptyList(),
    val trendingBattles: List<HomeContentModel> = emptyList(),
    val bestBattles: List<HomeContentModel> = emptyList(),
    val newBattles: List<HomeContentModel> = emptyList(),
    val todayPicks: List<TodayPickModel> = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        fetchHomeData()
    }

    private fun fetchHomeData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            homeRepository.fetchHomeData()
                .onSuccess { boardData ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            hasNewNotice = boardData.hasNewNotice,
                            editorPicks = boardData.editorPicks,
                            trendingBattles = boardData.trendingBattles,
                            bestBattles = boardData.bestBattles,
                            newBattles = boardData.newBattles,
                            todayPicks = boardData.todayPicks
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }
}
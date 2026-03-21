package com.swyp4.team2.ui.home

import android.util.Log
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
        Log.d("HomeFlow", "1. 홈 데이터 서버에 요청 시작!")
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            homeRepository.fetchHomeData()
                .onSuccess { boardData ->
                    Log.d("HomeFlow", "2. 🟢 홈 데이터 통신 성공! (에디터픽: ${boardData.editorPicks.size}개, 트렌딩: ${boardData.trendingBattles.size}개, 투데이픽: ${boardData.todayPicks.size}개)")
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
                    Log.e("HomeFlow", "2. 🔴 홈 데이터 통신 실패!", error)
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }
}
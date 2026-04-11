package com.picke.app.ui.todaybattle

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.app.domain.repository.ShareRepository
import com.picke.app.domain.repository.TodayBattleRepository
import com.picke.app.domain.repository.VoteRepository
import com.picke.app.ui.todaybattle.model.TodayBattleUiModel
import com.picke.app.ui.todaybattle.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TodayBattleUiState(
    val isLoading: Boolean = true,
    val battleList: List<TodayBattleUiModel> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class TodayBattleViewModel @Inject constructor(
    private val todayBattleRepository: TodayBattleRepository,
    private val voteRepository: VoteRepository,
    private val shareRepository: ShareRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodayBattleUiState())
    val uiState: StateFlow<TodayBattleUiState> = _uiState.asStateFlow()

    init {
        fetchTodayBattles()
    }

    fun submitPreVote(battleId: Long, optionId: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            voteRepository.submitPreVote(battleId, optionId)
                .onSuccess {
                    Log.d("VoteFlow", "🟢 사전 투표 성공! 배틀에 입장합니다.")
                    onSuccess()
                }
                .onFailure { error ->
                    Log.e("VoteFlow", "🔴 사전 투표 실패!", error)
                }
        }
    }

    private fun fetchTodayBattles() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            todayBattleRepository.fetchTodayBattles()
                .onSuccess { board ->
                    Log.d("BattleFlow", "🟢 배틀 목록 불러오기 성공! (${board.items.size}개)")
                    Log.d("BattleFlow", "🟢 배틀 목록 : (${board.items})")
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

    fun getShareLink(
        battleId: Int,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            shareRepository.getBattleShareLink(battleId)
                .onSuccess { shareUrl ->
                    Log.d("ShareFlow", "🟢 배틀 공유 링크 획득 성공! url: ${shareUrl.shareUrl}")
                    onSuccess(shareUrl.shareUrl)
                }
                .onFailure { error ->
                    Log.e("ShareFlow", "🔴 배틀 공유 링크 획득 실패!", error)
                    onError(error.message ?: "링크를 불러오는데 실패했습니다.")
                }
        }
    }
}
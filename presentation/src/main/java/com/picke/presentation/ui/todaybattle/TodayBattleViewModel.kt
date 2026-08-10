package com.picke.presentation.ui.todaybattle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.feature.battle.usecase.BattleUseCases
import com.picke.domain.feature.share.usecase.ShareUseCases
import com.picke.domain.feature.todaybattle.usecase.TodayBattleUseCases
import com.picke.domain.feature.vote.usecase.SubmitVoteResult
import com.picke.domain.feature.vote.usecase.VoteUseCases
import com.picke.presentation.ui.todaybattle.model.TodayBattleUiState
import com.picke.presentation.ui.todaybattle.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayBattleViewModel @Inject constructor(
    private val todayBattleUseCases: TodayBattleUseCases,
    private val voteUseCases: VoteUseCases,
    private val battleUseCases: BattleUseCases,
    private val shareUseCases: ShareUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodayBattleUiState())
    val uiState: StateFlow<TodayBattleUiState> = _uiState.asStateFlow()

    init {
        fetchTodayBattles()
    }

    fun enterBattle(
        battleId: Long,
        optionId: Long,
        onNavigateToScenario: (String) -> Unit,
        onNavigateToPerspective: (String) -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isEntering = true) }

            battleUseCases.getBattleStatusUseCase(battleId)
                .onSuccess { status ->
                    if (status.step == "NONE") {
                        submitPreVote(battleId, optionId, onNavigateToScenario)
                    } else {
                        _uiState.update { it.copy(isEntering = false) }
                        onNavigateToPerspective(battleId.toString())
                    }
                }
                .onFailure {
                    submitPreVote(battleId, optionId, onNavigateToScenario)
                }
        }
    }

    private suspend fun submitPreVote(
        battleId: Long,
        optionId: Long,
        onNavigateToScenario: (String) -> Unit
    ) {
        voteUseCases.submitVoteUseCase(
            battleId = battleId,
            optionId = optionId,
            isPreVote = true
        ).onSuccess { result ->
            when (result) {
                is SubmitVoteResult.Success -> {
                    _uiState.update { it.copy(isEntering = false) }
                    onNavigateToScenario(battleId.toString())
                }

                is SubmitVoteResult.InsufficientPoints -> {
                    _uiState.update {
                        it.copy(
                            isEntering = false,
                            errorMessage = "포인트가 부족합니다."
                        )
                    }
                }
            }
        }.onFailure { error ->
            _uiState.update {
                it.copy(
                    isEntering = false,
                    errorMessage = error.message
                )
            }
        }
    }

    private fun fetchTodayBattles() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            todayBattleUseCases.fetchTodayBattlesUseCase()
                .onSuccess { board ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            battleList = board.items.take(1).map { it.toUiModel() }
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message
                        )
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
            shareUseCases.getBattleShareLinkUseCase(battleId)
                .onSuccess { shareUrl ->
                    onSuccess(shareUrl.shareUrl)
                }
                .onFailure { error ->
                    onError(error.message ?: "링크를 불러오는데 실패했습니다.")
                }
        }
    }
}
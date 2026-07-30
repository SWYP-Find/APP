package com.picke.presentation.ui.todaybattle

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.usecase.battle.GetBattleStatusUseCase
import com.picke.domain.usecase.share.GetBattleShareLinkUseCase
import com.picke.domain.usecase.todaybattle.FetchTodayBattlesUseCase
import com.picke.domain.usecase.vote.SubmitVoteResult
import com.picke.domain.usecase.vote.SubmitVoteUseCase
import com.picke.presentation.ui.todaybattle.model.TodayBattleUiModel
import com.picke.presentation.ui.todaybattle.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TodayBattleUiState(
    val isLoading: Boolean = true,
    val isEntering: Boolean = false,
    val battleList: List<TodayBattleUiModel> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class TodayBattleViewModel @Inject constructor(
    private val fetchTodayBattlesUseCase: FetchTodayBattlesUseCase,
    private val submitVoteUseCase: SubmitVoteUseCase,
    private val getBattleStatusUseCase: GetBattleStatusUseCase,
    private val getBattleShareLinkUseCase: GetBattleShareLinkUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodayBattleUiState())
    val uiState: StateFlow<TodayBattleUiState> = _uiState.asStateFlow()

    init {
        fetchTodayBattles()
    }

    // "배틀 입장하기" 클릭 시: 참여 여부를 먼저 확인해서
    // 이미 참여한 배틀이면 관점 화면으로, 처음 참여하는 배틀이면 사전 투표 후 TTS(시나리오) 화면으로 보낸다.
    fun enterBattle(
        battleId: Long,
        optionId: Long,
        onNavigateToScenario: (String) -> Unit,
        onNavigateToPerspective: (String) -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isEntering = true) }

            getBattleStatusUseCase(battleId)
                .onSuccess { status ->
                    if (status.step == "NONE") {
                        submitPreVote(battleId, optionId, onNavigateToScenario)
                    } else {
                        Log.d("VoteFlow", "🟢 이미 참여한 배틀! 관점 화면으로 이동합니다. (step=${status.step})")
                        _uiState.update { it.copy(isEntering = false) }
                        onNavigateToPerspective(battleId.toString())
                    }
                }
                .onFailure { error ->
                    Log.e("VoteFlow", "🔴 배틀 참여 상태 확인 실패! 사전 투표를 시도합니다.", error)
                    submitPreVote(battleId, optionId, onNavigateToScenario)
                }
        }
    }

    private suspend fun submitPreVote(
        battleId: Long,
        optionId: Long,
        onNavigateToScenario: (String) -> Unit
    ) {
        submitVoteUseCase(battleId, optionId, isPreVote = true)
            .onSuccess { result ->
                when (result) {
                    is SubmitVoteResult.Success -> {
                        Log.d("VoteFlow", "🟢 사전 투표 성공! TTS 화면으로 이동합니다.")
                        _uiState.update { it.copy(isEntering = false) }
                        onNavigateToScenario(battleId.toString())
                    }

                    is SubmitVoteResult.InsufficientPoints -> {
                        Log.w("VoteFlow", "🟡 사전 투표 실패: 포인트 부족")
                        _uiState.update {
                            it.copy(
                                isEntering = false,
                                errorMessage = "포인트가 부족합니다."
                            )
                        }
                    }
                }
            }
            .onFailure { error ->
                Log.e("VoteFlow", "🔴 사전 투표 실패!", error)
                _uiState.update { it.copy(isEntering = false, errorMessage = error.message) }
            }
    }

    private fun fetchTodayBattles() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            fetchTodayBattlesUseCase()
                .onSuccess { board ->
                    Log.d("BattleFlow", "🟢 배틀 목록 불러오기 성공! (${board.items.size}개)")
                    Log.d("BattleFlow", "🟢 배틀 목록 : (${board.items})")
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            // 빠른 배틀은 하루 1개만 노출한다. (백엔드가 여러 개를 내려주더라도 앱에서 첫 번째 1개만 사용)
                            battleList = board.items.take(1).map { it.toUiModel() }
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
            getBattleShareLinkUseCase(battleId)
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
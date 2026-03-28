package com.swyp4.team2.ui.vote

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swyp4.team2.domain.model.BattleDetailBoard
import com.swyp4.team2.domain.model.VoteBoard
import com.swyp4.team2.domain.model.VoteOption
import com.swyp4.team2.domain.repository.BattleRepository
import com.swyp4.team2.domain.repository.VoteRepository
import com.swyp4.team2.ui.PhilosopherType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class VoteType {
    PRE,  // 사전 투표 (밝은 테마)
    POST  // 사후 투표 (어두운 테마)
}

data class VoteUiState(
    val isLoading: Boolean = false,
    val battleDetail: BattleDetailBoard? = null,
    val error: String? = null
)

@HiltViewModel
class VoteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val battleRepository: BattleRepository,
    private val voteRepository: VoteRepository
) : ViewModel() {

    val battleId: String = checkNotNull(savedStateHandle["battleId"])

    private val _uiState = MutableStateFlow(VoteUiState(isLoading = true))
    val uiState: StateFlow<VoteUiState> = _uiState.asStateFlow()

    init {
        fetchVoteDetail()
    }

    private fun fetchVoteDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val battleIdLong = battleId.toLongOrNull() ?: 0L
            Log.d("VoteDetailFlow", "1. 배틀 상세 정보 호출 시작 - battleIdLong: $battleIdLong")

            battleRepository.getBattleDetail(battleIdLong)
                .onSuccess { detailBoard ->
                    Log.d("VoteDetailFlow", "2. 데이터 통신 성공 ${detailBoard.battleInfo}")
                    _uiState.update {
                        it.copy(isLoading = false, battleDetail = detailBoard, error = null)
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(isLoading = false, error = error.message)
                    }
                }
        }
    }

    fun submitVote(voteType: VoteType, selectedOptionId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val optionIdLong = selectedOptionId.toLongOrNull() ?: 0L

            val result = if (voteType == VoteType.PRE) {
                voteRepository.submitPreVote(battleId, optionIdLong)
            } else {
                voteRepository.submitPostVote(battleId, optionIdLong)
            }

            result.onSuccess {
                onSuccess()
            }.onFailure { error ->
                // TODO: 에러 처리
            }
        }
    }
}
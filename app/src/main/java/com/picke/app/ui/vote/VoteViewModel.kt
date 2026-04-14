package com.picke.app.ui.vote

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.picke.app.domain.model.BattleDetailBoard
import com.picke.app.domain.repository.BattleRepository
import com.picke.app.domain.repository.ShareRepository
import com.picke.app.domain.repository.VoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
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
    private val voteRepository: VoteRepository,
    private val shareRepository: ShareRepository,
    private val mixpanel: MixpanelAPI
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
            val battleIdLong = battleId.toLongOrNull() ?: 0L

            Log.d("VoteDetailFlow", "투표 전송 시작 - type: $voteType, battleId: $battleIdLong, optionId: $optionIdLong")

            val result = if (voteType == VoteType.PRE) {
                voteRepository.submitPreVote(battleIdLong, optionIdLong)
            } else {
                voteRepository.submitPostVote(battleIdLong, optionIdLong)
            }

            result.onSuccess {
                Log.d("VoteDetailFlow", "🟢 투표 전송 성공!")

                try {
                    val props = JSONObject().apply {
                        put("battle_id", battleIdLong)
                        put("selected_option_id", optionIdLong)
                    }

                    if (voteType == VoteType.PRE) {
                        mixpanel.track("pre_vote", props)
                        Log.d("MixpanelFlow", "pre_vote 이벤트 전송 완료")
                    } else {
                        mixpanel.track("post_vote", props)
                        Log.d("MixpanelFlow", "post_vote 이벤트 전송 완료")
                    }
                } catch (e: Exception) {
                    Log.e("MixpanelFlow", "믹스패널 이벤트 전송 실패: ${e.message}")
                }

                // 기존 성공 콜백
                onSuccess()
            }.onFailure { error ->
                Log.e("VoteDetailFlow", "🔴 투표 전송 실패: ${error.message}", error)
                _uiState.update { it.copy(error = error.message) }
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
                    onSuccess(shareUrl.shareUrl)
                }
                .onFailure { error ->
                    onError(error.message ?: "링크를 불러오는데 실패했습니다.")
                }
        }
    }
}
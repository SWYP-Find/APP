package com.picke.presentation.ui.vote

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.common.local.LocalPreferencesUseCases
import com.picke.domain.feature.battle.usecase.BattleUseCases
import com.picke.domain.feature.share.usecase.ShareUseCases
import com.picke.domain.feature.vote.usecase.SubmitVoteResult
import com.picke.domain.feature.vote.usecase.VoteUseCases
import com.picke.presentation.ads.AdMobManager
import com.picke.presentation.analytics.AnalyticsTracker
import com.picke.presentation.analytics.BattleStepName
import com.picke.presentation.analytics.ShareTarget
import com.picke.presentation.ui.vote.model.VoteType
import com.picke.presentation.ui.vote.model.VoteUiState
import com.picke.presentation.ui.vote.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VoteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val battleUseCases: BattleUseCases,
    private val voteUseCases: VoteUseCases,
    private val shareUseCases: ShareUseCases,
    private val localPreferencesUseCases: LocalPreferencesUseCases,
    private val adMobManager: AdMobManager,
    private val analyticsTracker: AnalyticsTracker
) : ViewModel() {

    val battleId: String = checkNotNull(savedStateHandle["battleId"])

    private val _uiState = MutableStateFlow(VoteUiState(isLoading = true))
    val uiState: StateFlow<VoteUiState> = _uiState.asStateFlow()

    init {
        fetchVoteDetail()
        reloadAd()
    }

    fun reloadAd() {
        localPreferencesUseCases.getUserTag()?.let { tag ->
            adMobManager.loadAd(tag)
        }
    }

    private fun fetchVoteDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val battleIdLong = battleId.toLongOrNull() ?: 0L

            battleUseCases.getBattleDetailUseCase(battleIdLong)
                .onSuccess { detailBoard ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            battleDetail = detailBoard.toUiModel(),
                            error = null
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(isLoading = false, error = error.message)
                    }
                }
        }
    }

    fun submitVote(
        voteType: VoteType,
        selectedOptionId: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val optionIdLong = selectedOptionId.toLongOrNull() ?: 0L
            val battleIdLong = battleId.toLongOrNull() ?: 0L

            voteUseCases.submitVoteUseCase(
                battleId = battleIdLong,
                optionId = optionIdLong,
                isPreVote = voteType == VoteType.PRE
            )
                .onSuccess { result ->
                    when (result) {
                        is SubmitVoteResult.Success -> {
                            if (voteType == VoteType.PRE) {
                                analyticsTracker.trackBattleStep(
                                    stepName = BattleStepName.PRE_VOTE,
                                    contentId = battleId,
                                    choice = selectedOptionId
                                )
                            } else {
                                val isChanged = voteUseCases.getMyVoteHistoryUseCase(battleIdLong)
                                    .getOrNull()?.opinionChanged
                                analyticsTracker.trackBattleStep(
                                    stepName = BattleStepName.POST_VOTE,
                                    contentId = battleId,
                                    choice = selectedOptionId,
                                    isChanged = isChanged
                                )
                            }

                            _uiState.update { it.copy(isLoading = false) }
                            onSuccess()
                        }

                        is SubmitVoteResult.InsufficientPoints -> {
                            _uiState.update {
                                it.copy(isInsufficientPoints = true, isLoading = false)
                            }
                        }
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(error = error.message, isLoading = false) }
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

    fun dismissPointDialog() {
        _uiState.update { it.copy(isInsufficientPoints = false) }
    }

    fun trackShare(channel: String) {
        analyticsTracker.trackShareAction(ShareTarget.BATTLE, channel)
    }
}
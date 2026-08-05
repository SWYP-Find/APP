package com.picke.presentation.ui.my.makebattle

import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.common.local.LocalPreferencesUseCases
import com.picke.domain.feature.proposal.usecase.ProposalUseCases
import com.picke.domain.feature.proposal.usecase.SubmitProposalResult
import com.picke.presentation.ads.AdMobManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MakeBattleUiState(
    val isLoading: Boolean = false
)

sealed class MakeBattleEvent {
    object Success : MakeBattleEvent()
    object NotEnoughPoints : MakeBattleEvent()
    data class Error(val message: String) : MakeBattleEvent()
}

@HiltViewModel
class MakeBattleViewModel @Inject constructor(
    private val proposalUseCases: ProposalUseCases,
    val adMobManager: AdMobManager,
    private val localPreferencesUseCases: LocalPreferencesUseCases
) : ViewModel() {

    companion object {
        private const val TAG = "MakeBattleViewModel_Picke"
    }

    fun reloadAd() {
        localPreferencesUseCases.getUserTag()?.let { tag ->
            adMobManager.loadAd(tag)
        }
    }

    private val _uiState = MutableStateFlow(MakeBattleUiState())
    val uiState: StateFlow<MakeBattleUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<MakeBattleEvent>()
    val eventFlow: SharedFlow<MakeBattleEvent> = _eventFlow.asSharedFlow()

    // 제안하기 제출 함수
    fun submitProposal(
        category: String,
        topic: String,
        stanceA: String,
        stanceB: String,
        description: String
    ) {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            Log.d(TAG, "[FLOW] 배틀 주제 제안 API 호출 시작")
            _uiState.update { it.copy(isLoading = true) }

            proposalUseCases.submitProposalUseCase(
                category = category,
                topic = topic,
                stanceA = stanceA,
                stanceB = stanceB,
                description = description
            ).onSuccess { result ->
                _uiState.update { it.copy(isLoading = false) }

                when (result) {
                    is SubmitProposalResult.Success -> {
                        Log.i(TAG, "🟢 [SUCCESS] 배틀 제안 성공! ID: ${result.proposalId}")
                        _eventFlow.emit(MakeBattleEvent.Success)
                    }

                    is SubmitProposalResult.NotEnoughPoints -> {
                        _eventFlow.emit(MakeBattleEvent.NotEnoughPoints)
                    }
                }
            }.onFailure { error ->
                Log.e(TAG, "🔴 [ERROR] 배틀 제안 실패: ${error.message}", error)
                _uiState.update { it.copy(isLoading = false) }
                _eventFlow.emit(MakeBattleEvent.Error(error.message ?: "제안 제출에 실패했습니다."))
            }
        }
    }
}
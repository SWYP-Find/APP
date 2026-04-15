package com.picke.app.ui.my.makebattle

import retrofit2.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.app.data.local.TokenManager
import com.picke.app.di.AdMobManager
import com.picke.app.domain.repository.ProposalRepository
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
    private val proposalRepository: ProposalRepository,
    val adMobManager: AdMobManager,
    private val tokenManager: TokenManager
) : ViewModel() {

    companion object {
        private const val TAG = "MakeBattleViewModel_Picke"
    }

    fun reloadAd() {
        tokenManager.getUserTag()?.let { tag ->
            adMobManager.loadAd(tag)
        }
    }

    private val _uiState = MutableStateFlow(MakeBattleUiState())
    val uiState: StateFlow<MakeBattleUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<MakeBattleEvent>()
    val eventFlow: SharedFlow<MakeBattleEvent> = _eventFlow.asSharedFlow()

    // 제안하기 제출 함수
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
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

            val result = proposalRepository.submitProposal(
                category = category,
                topic = topic,
                positionA = stanceA,
                positionB = stanceB,
                description = description
            )

            result.onSuccess { boardData ->
                Log.i(TAG, "🟢 [SUCCESS] 배틀 제안 성공! ID: ${boardData.id}")
                _uiState.update { it.copy(isLoading = false) }

                _eventFlow.emit(MakeBattleEvent.Success)
            }.onFailure { error ->
                Log.e(TAG, "🔴 [ERROR] 배틀 제안 실패: ${error.message}", error)
                _uiState.update { it.copy(isLoading = false) }

                if (error is HttpException && error.code() == 400) {
                    _eventFlow.emit(MakeBattleEvent.NotEnoughPoints)
                } else {
                    _eventFlow.emit(MakeBattleEvent.Error(error.message ?: "제안 제출에 실패했습니다."))
                }
            }
        }
    }
}
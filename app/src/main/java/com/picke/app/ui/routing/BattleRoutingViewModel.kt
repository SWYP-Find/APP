package com.picke.app.ui.routing

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.app.domain.usecase.battle.GetBattleStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "BattleRoutingFlow"

@HiltViewModel
class BattleRoutingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getBattleStatusUseCase: GetBattleStatusUseCase
) : ViewModel() {
    val battleId: String = checkNotNull(savedStateHandle["battleId"])

    private val _routeEvent = MutableStateFlow<String?>(null)
    val routeEvent: StateFlow<String?> = _routeEvent.asStateFlow()

    init {
        checkBattleStatus()
    }

    private fun checkBattleStatus() {
        viewModelScope.launch {
            Log.d(TAG, "🔍 [배틀 상태 확인] 요청 시작 - battleId: $battleId")

            getBattleStatusUseCase(battleId.toLongOrNull() ?: 0L)
                .onSuccess { statusBoard ->
                    Log.d(TAG, "🟢 [배틀 상태 확인] 서버 통신 성공! - 현재 상태(step): ${statusBoard.step}")

                    when (statusBoard.step) {
                        "COMPLETED" -> {
                            Log.d(TAG, "➡️ COMPLETE 상태 확인됨 -> 관점(PERSPECTIVE) 화면으로 이동합니다.")
                            _routeEvent.value = "PERSPECTIVE"
                        }
                        "NONE", "PRE_VOTE", "SCENARIO" -> {
                            Log.d(TAG, "➡️ ${statusBoard.step} 상태 확인됨 -> 사전투표(PRE_VOTE) 화면으로 이동합니다.")
                            _routeEvent.value = "PRE_VOTE"
                        }
                        else -> {
                            Log.w(TAG, "🟡 알 수 없는 상태(${statusBoard.step}) 확인됨 -> 기본값인 사전투표(PRE_VOTE) 화면으로 이동합니다.")
                            _routeEvent.value = "PRE_VOTE"
                        }
                    }
                }
                .onFailure { error ->
                    Log.e(TAG, "🔴 [배틀 상태 확인] 서버 통신 실패 - 에러: ${error.message}", error)
                    _routeEvent.value = "PRE_VOTE"
                }
        }
    }
}

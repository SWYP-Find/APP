package com.swyp4.team2.ui.routing

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.swyp4.team2.domain.repository.BattleRepository
import com.swyp4.team2.ui.theme.Primary900
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "BattleRoutingFlow"

// --- ViewModel ---
@HiltViewModel
class BattleRoutingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val battleRepository: BattleRepository
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

            battleRepository.getBattleStatus(battleId.toLongOrNull() ?: 0L)
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

// --- Screen ---
@Composable
fun BattleRoutingScreen(
    onNavigateToPreVote: (String) -> Unit,
    onNavigateToPerspective: (String) -> Unit,
    viewModel: BattleRoutingViewModel = hiltViewModel()
) {
    val routeEvent = viewModel.routeEvent.collectAsStateWithLifecycle().value

    // 상태값이 바뀌면 즉시 화면 이동 (라우팅 화면은 백스택에서 지워지도록 AppNavigation에서 처리)
    LaunchedEffect(routeEvent) {
        when (routeEvent) {
            "PRE_VOTE" -> onNavigateToPreVote(viewModel.battleId)
            "PERSPECTIVE" -> onNavigateToPerspective(viewModel.battleId)
        }
    }

    // API 결과를 기다리는 아주 짧은 시간 동안 보여줄 로딩 화면
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Primary900)
    }
}
package com.swyp4.team2.ui.scenario

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swyp4.team2.domain.repository.ScenarioRepository
import com.swyp4.team2.ui.scenario.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ScenarioUiState(
    val title: String = "뒤샹의 변기, 예술인가 도발인가", // 배틀 제목
    val scripts: List<ScenarioScriptUiModel> = emptyList(), // 현재 화면에 뿌릴 대사 목록
    val activeIndex: Int = -1, // 현재 타이밍에 맞는 대사 인덱스
    val isPlaying: Boolean = false,
    val currentPositionMs: Long = 0L,
    val totalDurationMs: Long = 0L,
    val interactiveOptions: List<ScenarioOptionUiModel> = emptyList(),
    val currentNodeId: String = ""
)

@HiltViewModel
class ScenarioViewModel @Inject constructor(
    private val scenarioRepository: ScenarioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScenarioUiState())
    val uiState: StateFlow<ScenarioUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var fullScenario: ScenarioUiModel? = null

    // 1. 시나리오 데이터 로드
    fun loadScenario(battleId: String) {
        viewModelScope.launch {
            Log.d("ScenarioFlow", "▶️ API 호출 시작! (battleId: $battleId)")

            scenarioRepository.fetchBattleScenario(battleId)
                .onSuccess { board ->
                    // 🌟 2. 통신 대성공! (가져온 데이터 전체 출력)
                    Log.d("ScenarioFlow", "✅ API 호출 성공! 시작 노드: ${board.startNodeId}")
                    Log.d("ScenarioFlow", "✅ 노드 개수: ${board.nodes.size}개")
                    Log.d("ScenarioFlow", "✅ 원본 데이터: $board")

                    fullScenario = board.toUiModel()
                    val firstNodeId = fullScenario?.startNodeId ?: return@onSuccess
                    loadNode(firstNodeId)
                }
                .onFailure { error ->
                    // 🌟 3. 통신 실패! (에러 원인 출력)
                    Log.e("ScenarioFlow", "❌ API 호출 실패... 이유를 찾아보자!", error)
                }
        }
    }

    // 2. 특정 챕터(Node) 로드
    private fun loadNode(nodeId: String) {
        val node = fullScenario?.nodes?.get(nodeId) ?: return

        _uiState.update { it.copy(
            currentNodeId = nodeId,
            scripts = node.scripts,
            totalDurationMs = node.audioDuration * 1000L,
            currentPositionMs = 0L,
            activeIndex = -1,
            interactiveOptions = emptyList() // 새로운 노드 시작시 선택지 초기화
        )}

        // TODO: 여기서 실제 오디오 플레이어(ExoPlayer 등)에 URL을 세팅하고 재생 준비!
    }

    // 3. 타이머 진행에 따른 싱크 맞추기
    private fun updateSync(positionMs: Long) {
        val scripts = _uiState.value.scripts

        // 현재 시간에 해당하는 가장 최근 대사를 찾습니다.
        val newActiveIndex = scripts.indexOfLast { it.startTimeMs <= positionMs }

        _uiState.update { it.copy(
            currentPositionMs = positionMs,
            activeIndex = newActiveIndex
        )}

        // 노드(챕터)가 끝났는지 확인
        if (positionMs >= _uiState.value.totalDurationMs) {
            handleNodeEnd()
        }
    }

    // 4. 챕터가 끝났을 때의 분기 처리
    private fun handleNodeEnd() {
        pauseAudio()
        val currentNode = fullScenario?.nodes?.get(_uiState.value.currentNodeId) ?: return

        if (currentNode.autoNextNodeId != null) {
            // 선택지 없이 자동으로 다음으로 넘어가야 할 때
            loadNode(currentNode.autoNextNodeId)
            playAudio()
        } else if (currentNode.interactiveOptions.isNotEmpty()) {
            // 선택지를 화면에 띄워야 할 때 (A/B 버튼)
            _uiState.update { it.copy(interactiveOptions = currentNode.interactiveOptions) }
        }
    }

    // 5. 플레이어 컨트롤
    fun togglePlayPause() {
        if (_uiState.value.isPlaying) pauseAudio() else playAudio()
    }

    private fun playAudio() {
        _uiState.update { it.copy(isPlaying = true) }
        // 🚨 실제로는 ExoPlayer의 currentPosition을 가져와야 하지만, 임시 타이머로 구현
        timerJob = viewModelScope.launch {
            while (true) {
                delay(100)
                updateSync(_uiState.value.currentPositionMs + 100)
            }
        }
    }

    private fun pauseAudio() {
        _uiState.update { it.copy(isPlaying = false) }
        timerJob?.cancel()
    }

    fun selectOption(nextNodeId: String) {
        loadNode(nextNodeId)
        playAudio()
    }

    fun seekToPosition(ratio: Float) {
        val newPos = (_uiState.value.totalDurationMs * ratio).toLong()
        updateSync(newPos)
    }

    fun seekRewind() { updateSync(maxOf(0, _uiState.value.currentPositionMs - 15000)) }
    fun seekForward() { updateSync(minOf(_uiState.value.totalDurationMs, _uiState.value.currentPositionMs + 15000)) }
}
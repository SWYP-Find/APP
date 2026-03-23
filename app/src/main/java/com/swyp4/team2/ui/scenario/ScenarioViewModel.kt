package com.swyp4.team2.ui.scenario

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.swyp4.team2.domain.repository.ScenarioRepository
import com.swyp4.team2.ui.scenario.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ScenarioUiState(
    val title: String = "뒤샹의 변기, 예술인가 도발인가",
    val scripts: List<ScenarioScriptUiModel> = emptyList(),
    val activeIndex: Int = -1,
    val isPlaying: Boolean = false,
    val currentPositionMs: Long = 0L,
    val totalDurationMs: Long = 0L,
    val interactiveOptions: List<ScenarioOptionUiModel> = emptyList(),
    val currentNodeId: String = "",
    val showOptions: Boolean = false
)

@HiltViewModel
class ScenarioViewModel @Inject constructor(
    private val scenarioRepository: ScenarioRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScenarioUiState())
    val uiState: StateFlow<ScenarioUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var fullScenario: ScenarioUiModel? = null

    // 1. 오디오 플레이어
    private val player = ExoPlayer.Builder(context).build().apply {
        addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                // 오디오가 끝까지 재생되었을 때 노드 종료 처리
                if (playbackState == Player.STATE_ENDED) {
                    handleNodeEnd()
                }
            }
            override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                Log.e("ScenarioFlow", "🚨 앗! ExoPlayer 에러 발생: ${error.message}", error)
            }
        })
    }

    // 2. 시나리오 데이터 로드
    fun loadScenario(battleId: String) {
        viewModelScope.launch {
            Log.d("ScenarioFlow", "▶️ API 호출 시작! (battleId: $battleId)")

            scenarioRepository.fetchBattleScenario(battleId)
                .onSuccess { board ->
                    Log.d("ScenarioFlow", "✅ API 호출 성공! 시작 노드: ${board.startNodeId}")
                    Log.d("ScenarioFlow", "==========================================")
                    Log.d("ScenarioFlow", "🚀 배틀 ID: ${board.battleId}")
                    Log.d("ScenarioFlow", "🔗 오디오 목록: ${board.audios}")
                    Log.d("ScenarioFlow", "🛣️ 추천 경로: ${board.recommendedPathKey}")
                    Log.d("ScenarioFlow", "📦 전체 노드 개수: ${board.nodes.size}개")

                    fullScenario = board.toUiModel()
                    val firstNodeId = fullScenario?.startNodeId ?: return@onSuccess
                    loadNode(firstNodeId)
                }
                .onFailure { error ->
                    Log.e("ScenarioFlow", "❌ API 호출 실패...", error)
                }
        }
    }

    // 3. 특정 챕터(Node) 로드 및 오디오 준비
    private fun loadNode(nodeId: String) {
        val node = fullScenario?.nodes?.get(nodeId) ?: return

        // 권장 경로(COMMON)의 오디오를 찾거나, 없으면 있는 오디오 중 첫 번째 것을 가져옴
        val audioUrl = fullScenario?.audios?.get("COMMON") ?: fullScenario?.audios?.values?.firstOrNull()

        _uiState.update { it.copy(
            currentNodeId = nodeId,
            scripts = node.scripts,
            totalDurationMs = node.audioDuration * 1000L,
            currentPositionMs = 0L,
            activeIndex = -1,
            showOptions = false,
            interactiveOptions = emptyList()
        )}

        // URL이 있으면 플레이어에 세팅
        if (audioUrl != null) {
            val mediaItem = MediaItem.fromUri(audioUrl)
            player.setMediaItem(mediaItem)
            player.prepare()

            playAudio()
        }
    }

    // 4. 싱크 맞추기 (실제 플레이어 시간 기반)
    private fun updateSync(positionMs: Long) {
        val scripts = _uiState.value.scripts
        val newActiveIndex = scripts.indexOfLast { it.startTimeMs <= positionMs }

        _uiState.update { it.copy(
            currentPositionMs = positionMs,
            activeIndex = newActiveIndex,
            // 오디오가 99% 끝났을 때 옵션을 보여주기 위한 여유값(100ms)
            showOptions = positionMs >= _uiState.value.totalDurationMs - 100
        )}

        // 혹시라도 STATE_ENDED 이벤트가 안 탈 경우를 대비한 안전장치
        if (positionMs >= _uiState.value.totalDurationMs && _uiState.value.isPlaying) {
            handleNodeEnd()
        }
    }

    // 5. 챕터가 끝났을 때의 분기 처리
    private fun handleNodeEnd() {
        pauseAudio()
        val currentNode = fullScenario?.nodes?.get(_uiState.value.currentNodeId) ?: return

        if (currentNode.autoNextNodeId != null) {
            loadNode(currentNode.autoNextNodeId)
            playAudio()
        } else if (currentNode.interactiveOptions.isNotEmpty()) {
            _uiState.update { it.copy(interactiveOptions = currentNode.interactiveOptions) }
        }
    }

    // 6. 플레이어 컨트롤 (재생 / 정지 / 이동)
    fun togglePlayPause() {
        if (_uiState.value.isPlaying) pauseAudio() else playAudio()
    }

    private fun playAudio() {
        _uiState.update { it.copy(isPlaying = true) }
        player.play()

        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(100)
                updateSync(player.currentPosition)
            }
        }
    }

    private fun pauseAudio() {
        _uiState.update { it.copy(isPlaying = false) }
        player.pause()
        timerJob?.cancel()
    }

    fun selectOption(nextNodeId: String) {
        loadNode(nextNodeId)
        playAudio()
    }

    fun seekToPosition(ratio: Float) {
        val newPos = (_uiState.value.totalDurationMs * ratio).toLong()
        player.seekTo(newPos)
        updateSync(newPos)
    }

    fun seekRewind() {
        val newPos = maxOf(0, player.currentPosition - 15000)
        player.seekTo(newPos)
        updateSync(newPos)
    }

    fun seekForward() {
        val newPos = minOf(player.duration, player.currentPosition + 15000)
        player.seekTo(newPos)
        updateSync(newPos)
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}
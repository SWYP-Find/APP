package com.picke.app.ui.scenario

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.app.BuildConfig
import com.picke.app.domain.repository.ScenarioRepository
import com.picke.app.ui.scenario.model.*
import com.picke.app.util.ScenarioAudioKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PastChoice(
    val scriptIndex: Int,
    val options: List<ScenarioOptionUiModel>,
    val selectedNextNodeId: String
)

data class ScenarioUiState(
    val title: String = "",
    val pastScripts: List<ScenarioScriptUiModel> = emptyList(),
    val pastChoices: List<PastChoice> = emptyList(),
    val scripts: List<ScenarioScriptUiModel> = emptyList(),
    val activeIndex: Int = -1,
    val maxRevealedIndex: Int = -1,
    val nodeEndTimeMs: Long = 0L,
    val isPlaying: Boolean = false,
    val currentPositionMs: Long = 0L,
    val maxListenedPositionMs: Long = 0L,
    val totalDurationMs: Long = 0L,
    val interactiveOptions: List<ScenarioOptionUiModel> = emptyList(),
    val currentNodeId: String = "",
    val showOptions: Boolean = false,
    val showFinalVoteDialog: Boolean = false,
    val isReviewing: Boolean = false
)

@HiltViewModel
class ScenarioViewModel @Inject constructor(
    private val scenarioRepository: ScenarioRepository,
    private val audioPlayerManager: AudioPlayerManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScenarioUiState())
    val uiState: StateFlow<ScenarioUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var fullScenario: ScenarioUiModel? = null
    private var currentAudioKey: String? = null

    init {
        audioPlayerManager.onPlaybackEnded = { handleNodeEnd() }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun loadScenario(battleId: String) {
        Log.d("TTSFlow", "▶️ loadScenario() 실행 - 요청된 battleId: $battleId")
        viewModelScope.launch {
            _uiState.update { it.copy(pastScripts = emptyList(), pastChoices = emptyList(), maxListenedPositionMs = 0L) }
            scenarioRepository.fetchBattleScenario(battleId)
                .onSuccess { board ->
                    Log.d("TTSFlow", "▶️ loadScenario() 성공 - 서버에서 데이터 받아옴")
                    fullScenario = board.toUiModel()
                    _uiState.update { it.copy(title = board.title) }
                    val firstNodeId = fullScenario?.startNodeId ?: return@onSuccess
                    loadNode(firstNodeId)
                }
                .onFailure { error ->
                    Log.e("TTSFlow", "🚨 loadScenario() API 호출 실패: ${error.message}")
                }
        }
    }

    private fun loadNode(nodeId: String) {
        Log.d("TTSFlow", "▶️ loadNode() 실행 - 타겟 nodeId: $nodeId")
        val node = fullScenario?.nodes?.get(nodeId) ?: return

        val targetKey = when {
            node.nodeName.contains(ScenarioAudioKey.NODE_SUFFIX_A) -> ScenarioAudioKey.PATH_A
            node.nodeName.contains(ScenarioAudioKey.NODE_SUFFIX_B) -> ScenarioAudioKey.PATH_B
            else -> currentAudioKey ?: fullScenario?.audios?.keys?.firstOrNull()
        }

        Log.d("TTSFlow", "▶️ loadNode() 판단 - 타겟 오디오 키: $targetKey, 현재 오디오 키: $currentAudioKey")

        if (targetKey != null && targetKey != currentAudioKey) {
            Log.d("TTSFlow", "▶️ loadNode() - 새로운 오디오 파일 로드 및 준비")
            val audioUrl = fullScenario?.audios?.get(targetKey)
            if (audioUrl != null) {
                currentAudioKey = targetKey
                val fullAudioUrl = if (audioUrl.startsWith("http")) audioUrl else BuildConfig.BASE_URL + audioUrl
                audioPlayerManager.loadAudio(fullAudioUrl, _uiState.value.currentPositionMs)
            }
        }

        val sortedScripts = node.scripts.sortedBy { it.startTimeMs }
        val startMs = sortedScripts.firstOrNull()?.startTimeMs ?: _uiState.value.currentPositionMs
        val endMs = startMs + (node.audioDuration * 1000L)
        Log.d("TTSFlow", "▶️ loadNode() - 노드 재생 구간 계산 (start: $startMs, end: $endMs)")

        val splitScripts = splitScriptsBySentence(sortedScripts, endMs)

        val currentScripts = _uiState.value.scripts
        val newPastScripts = if (currentScripts.isNotEmpty() && _uiState.value.currentNodeId != nodeId) {
            _uiState.value.pastScripts + currentScripts
        } else {
            _uiState.value.pastScripts
        }

        _uiState.update { it.copy(
            currentNodeId = nodeId,
            pastScripts = newPastScripts,
            scripts = splitScripts,
            nodeEndTimeMs = endMs,
            activeIndex = -1,
            maxRevealedIndex = -1,
            showOptions = false,
            interactiveOptions = emptyList()
        )}

        playAudio()
    }

    private fun updateSync(positionMs: Long) {
        val scripts = _uiState.value.scripts
        val newActiveIndex = scripts.indexOfLast { it.startTimeMs <= positionMs }
        val newMaxRevealed = maxOf(_uiState.value.maxRevealedIndex, newActiveIndex)
        val newMaxListened = maxOf(_uiState.value.maxListenedPositionMs, positionMs)
        val isNodeEnded = positionMs >= _uiState.value.nodeEndTimeMs

        _uiState.update { it.copy(
            currentPositionMs = positionMs,
            maxListenedPositionMs = newMaxListened,
            totalDurationMs = audioPlayerManager.duration,
            activeIndex = newActiveIndex,
            maxRevealedIndex = newMaxRevealed,
            showOptions = isNodeEnded
        )}

        if (isNodeEnded && _uiState.value.isPlaying) {
            handleNodeEnd()
        }
    }

    private fun handleNodeEnd() {
        Log.d("TTSFlow", "▶️ handleNodeEnd() 실행 - 노드 끝 도달")
        pauseAudio()
        val currentNode = fullScenario?.nodes?.get(_uiState.value.currentNodeId) ?: return

        if (_uiState.value.isReviewing) {
            Log.d("TTSFlow", "▶️ handleNodeEnd() - 리뷰 모드이므로 여기서 재생 종료")
            return
        }

        if (currentNode.autoNextNodeId != null) {
            Log.d("TTSFlow", "▶️ handleNodeEnd() - 자동 다음 노드(${currentNode.autoNextNodeId})로 이동")
            loadNode(currentNode.autoNextNodeId)
            playAudio()
        } else if (currentNode.interactiveOptions.isNotEmpty()) {
            Log.d("TTSFlow", "▶️ handleNodeEnd() - 선택지 띄우기")
            _uiState.update { it.copy(interactiveOptions = currentNode.interactiveOptions) }
        } else {
            Log.d("TTSFlow", "▶️ handleNodeEnd() - 시나리오 최종 완료, 투표 다이얼로그 띄우기")
            _uiState.update { it.copy(showFinalVoteDialog = true) }
        }
    }

    fun dismissFinalDialog() {
        _uiState.update { it.copy(showFinalVoteDialog = false) }
    }

    fun restartCurrentAudio() {
        Log.d("TTSFlow", "▶️ restartCurrentAudio() 실행 - 오디오 처음부터 다시 재생")
        val allScripts = _uiState.value.pastScripts + _uiState.value.scripts

        _uiState.update { it.copy(
            showFinalVoteDialog = false,
            isReviewing = true,
            pastScripts = emptyList(),
            scripts = allScripts,
            maxRevealedIndex = allScripts.size - 1
        )}

        audioPlayerManager.seekTo(0)
        updateSync(0)
        playAudio()
    }

    fun togglePlayPause() {
        Log.d("TTSFlow", "▶️ togglePlayPause() 실행 - 현재 isPlaying: ${_uiState.value.isPlaying}")
        if (_uiState.value.isPlaying) pauseAudio() else playAudio()
    }

    private fun playAudio() {
        Log.d("TTSFlow", "▶️ playAudio() 실행 - 오디오 재생 시작")
        _uiState.update { it.copy(isPlaying = true) }
        audioPlayerManager.play()

        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(100)
                updateSync(audioPlayerManager.currentPosition)
            }
        }
    }

    private fun pauseAudio() {
        Log.d("TTSFlow", "▶️ pauseAudio() 실행 - 오디오 일시정지")
        _uiState.update { it.copy(isPlaying = false) }
        audioPlayerManager.pause()
        timerJob?.cancel()
    }

    fun seekRewind() {
        Log.d("TTSFlow", "▶️ seekRewind() 실행 - 15초 뒤로 가기")
        val newPos = maxOf(0, audioPlayerManager.currentPosition - 15000)
        audioPlayerManager.seekTo(newPos)
        updateSync(newPos)
        if (!_uiState.value.isPlaying) playAudio()
    }

    fun seekToPosition(ratio: Float) {
        Log.d("TTSFlow", "▶️ seekToPosition() 실행 - 요청 비율: $ratio")
        val newPos = (_uiState.value.totalDurationMs * ratio).toLong()
        val safePos = minOf(newPos, _uiState.value.nodeEndTimeMs, _uiState.value.maxListenedPositionMs)
        audioPlayerManager.seekTo(safePos)
        updateSync(safePos)
        if (!_uiState.value.isPlaying) playAudio()
    }

    fun seekForward() {
        Log.d("TTSFlow", "▶️ seekForward() 실행 - 15초 앞으로 가기")
        if (_uiState.value.showOptions || _uiState.value.showFinalVoteDialog) {
            Log.d("TTSFlow", "▶️ seekForward() 방어 - 선택 중이므로 넘어갈 수 없습니다.")
            return
        }
        val safePos = minOf(audioPlayerManager.currentPosition + 15000, _uiState.value.nodeEndTimeMs)
        audioPlayerManager.seekTo(safePos)
        updateSync(safePos)
        if (!_uiState.value.isPlaying) playAudio()
    }

    fun selectOption(nextNodeId: String) {
        Log.d("TTSFlow", "▶️ selectOption() 실행 - 사용자가 옵션 선택: $nextNodeId")
        val currentOptions = _uiState.value.interactiveOptions
        val totalScriptsSize = _uiState.value.pastScripts.size + _uiState.value.scripts.size

        val newChoice = PastChoice(
            scriptIndex = totalScriptsSize - 1,
            options = currentOptions,
            selectedNextNodeId = nextNodeId
        )

        _uiState.update { it.copy(pastChoices = it.pastChoices + newChoice) }
        loadNode(nextNodeId)
        playAudio()
    }

    private fun splitScriptsBySentence(
        originalScripts: List<ScenarioScriptUiModel>,
        totalNodeDurationMs: Long
    ): List<ScenarioScriptUiModel> {
        Log.d("TTSFlow", "▶️ splitScriptsBySentence() 실행 - 스크립트를 문장 단위로 분할")
        val result = mutableListOf<ScenarioScriptUiModel>()

        for (i in originalScripts.indices) {
            val current = originalScripts[i]
            val nextTimeMs = if (i < originalScripts.lastIndex) originalScripts[i + 1].startTimeMs else totalNodeDurationMs

            val sentences = current.displayText
                .split(Regex("(?<=[.!?])\\s+"))
                .map { it.trim() }
                .filter { it.isNotEmpty() }

            if (sentences.isEmpty()) continue

            val totalChars = sentences.sumOf { it.length }
            val blockDuration = maxOf(0L, nextTimeMs - current.startTimeMs)
            var accumulatedTime = current.startTimeMs

            sentences.forEach { sentence ->
                result.add(current.copy(startTimeMs = accumulatedTime, displayText = sentence))
                val durationForThisSentence = if (totalChars > 0) (blockDuration * sentence.length) / totalChars else 0L
                accumulatedTime += durationForThisSentence
            }
        }
        return result
    }

    override fun onCleared() {
        Log.d("TTSFlow", "▶️ onCleared() 실행 - 뷰모델 소멸 및 플레이어 해제")
        super.onCleared()
        audioPlayerManager.release()
    }
}
package com.picke.presentation.ui.scenario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.feature.scenario.usecase.ScenarioUseCases
import com.picke.presentation.BuildConfig
import com.picke.presentation.analytics.AnalyticsTracker
import com.picke.presentation.analytics.BattleStepName
import com.picke.presentation.ui.scenario.model.PastChoice
import com.picke.presentation.ui.scenario.model.ScenarioUiModel
import com.picke.presentation.ui.scenario.model.ScenarioUiState
import com.picke.presentation.ui.scenario.model.toUiModel
import com.picke.presentation.util.ScenarioAudioKey
import com.picke.presentation.util.splitScriptsBySentence
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScenarioViewModel @Inject constructor(
    private val scenarioUseCases: ScenarioUseCases,
    private val audioPlayerManager: AudioPlayerManager,
    private val analyticsTracker: AnalyticsTracker
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScenarioUiState())
    val uiState: StateFlow<ScenarioUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var fullScenario: ScenarioUiModel? = null
    private var currentAudioKey: String? = null
    private var currentBattleId: String = ""
    private var isAudioEndTracked = false

    init {
        audioPlayerManager.onPlaybackEnded = { handleNodeEnd() }
    }

    fun loadScenario(battleId: String) {
        currentBattleId = battleId
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    pastScripts = emptyList(),
                    pastChoices = emptyList(),
                    maxListenedPositionMs = 0L
                )
            }
            scenarioUseCases.fetchBattleScenarioUseCase(battleId)
                .onSuccess { board ->
                    fullScenario = board.toUiModel()
                    _uiState.update { it.copy(title = board.title) }
                    val firstNodeId = fullScenario?.startNodeId ?: return@onSuccess
                    loadNode(firstNodeId)
                }
        }
    }

    private fun loadNode(nodeId: String) {
        val node = fullScenario?.nodes?.get(nodeId) ?: return

        val targetKey = when {
            node.nodeName.contains(ScenarioAudioKey.NODE_SUFFIX_A) -> ScenarioAudioKey.PATH_A
            node.nodeName.contains(ScenarioAudioKey.NODE_SUFFIX_B) -> ScenarioAudioKey.PATH_B
            else -> currentAudioKey ?: fullScenario?.audios?.keys?.firstOrNull()
        }

        if (targetKey != null && targetKey != currentAudioKey) {
            val audioUrl = fullScenario?.audios?.get(targetKey)
            if (audioUrl != null) {
                currentAudioKey = targetKey
                val fullAudioUrl =
                    if (audioUrl.startsWith("http")) audioUrl else BuildConfig.BASE_URL + audioUrl
                audioPlayerManager.loadAudio(fullAudioUrl, _uiState.value.currentPositionMs)
            }
        }

        val sortedScripts = node.scripts.sortedBy { it.startTimeMs }
        val startMs = sortedScripts.firstOrNull()?.startTimeMs ?: _uiState.value.currentPositionMs
        val endMs = startMs + (node.audioDuration * 1000L)

        val splitScripts = splitScriptsBySentence(sortedScripts, endMs)

        val currentScripts = _uiState.value.scripts
        val newPastScripts =
            if (currentScripts.isNotEmpty() && _uiState.value.currentNodeId != nodeId) {
                _uiState.value.pastScripts + currentScripts
            } else {
                _uiState.value.pastScripts
            }

        _uiState.update {
            it.copy(
                currentNodeId = nodeId,
                pastScripts = newPastScripts,
                scripts = splitScripts,
                nodeEndTimeMs = endMs,
                activeIndex = -1,
                maxRevealedIndex = -1,
                showOptions = false,
                interactiveOptions = emptyList()
            )
        }

        playAudio()
    }

    private fun updateSync(positionMs: Long) {
        val scripts = _uiState.value.scripts
        val newActiveIndex = scripts.indexOfLast { it.startTimeMs <= positionMs }
        val newMaxRevealed = maxOf(_uiState.value.maxRevealedIndex, newActiveIndex)
        val newMaxListened = maxOf(_uiState.value.maxListenedPositionMs, positionMs)
        val isNodeEnded = positionMs >= _uiState.value.nodeEndTimeMs

        _uiState.update {
            it.copy(
                currentPositionMs = positionMs,
                maxListenedPositionMs = newMaxListened,
                totalDurationMs = audioPlayerManager.duration,
                activeIndex = newActiveIndex,
                maxRevealedIndex = newMaxRevealed,
                showOptions = isNodeEnded
            )
        }

        if (isNodeEnded && _uiState.value.isPlaying) {
            handleNodeEnd()
        }
    }

    private fun handleNodeEnd() {
        pauseAudio()
        val currentNode = fullScenario?.nodes?.get(_uiState.value.currentNodeId) ?: return

        if (_uiState.value.isReviewing) return

        if (currentNode.autoNextNodeId != null) {
            loadNode(currentNode.autoNextNodeId)
            playAudio()
        } else if (currentNode.interactiveOptions.isNotEmpty()) {
            _uiState.update { it.copy(interactiveOptions = currentNode.interactiveOptions) }
        } else {
            if (!isAudioEndTracked && currentBattleId.isNotEmpty()) {
                isAudioEndTracked = true
                analyticsTracker.trackBattleStep(BattleStepName.AUDIO_END, currentBattleId)
            }
            _uiState.update { it.copy(showFinalVoteDialog = true) }
        }
    }

    fun dismissFinalDialog() {
        _uiState.update { it.copy(showFinalVoteDialog = false) }
    }

    fun restartCurrentAudio() {
        val allScripts = _uiState.value.pastScripts + _uiState.value.scripts

        _uiState.update {
            it.copy(
                showFinalVoteDialog = false,
                isReviewing = true,
                pastScripts = emptyList(),
                scripts = allScripts,
                maxRevealedIndex = allScripts.size - 1
            )
        }

        audioPlayerManager.seekTo(0)
        updateSync(0)
        playAudio()
    }

    fun togglePlayPause() {
        if (_uiState.value.isPlaying) pauseAudio() else playAudio()
    }

    private fun playAudio() {
        _uiState.update { it.copy(isPlaying = true) }
        audioPlayerManager.play()

        timerJob = viewModelScope.launch {
            while (isActive) {
                updateSync(audioPlayerManager.currentPosition)
            }
        }
    }

    private fun pauseAudio() {
        _uiState.update { it.copy(isPlaying = false) }
        audioPlayerManager.pause()
        timerJob?.cancel()
    }

    fun seekRewind() {
        val newPos = maxOf(0, audioPlayerManager.currentPosition - 15000)
        audioPlayerManager.seekTo(newPos)
        updateSync(newPos)
        if (!_uiState.value.isPlaying) playAudio()
    }

    fun seekToPosition(ratio: Float) {
        val newPos = (_uiState.value.totalDurationMs * ratio).toLong()
        val safePos =
            minOf(newPos, _uiState.value.nodeEndTimeMs, _uiState.value.maxListenedPositionMs)
        audioPlayerManager.seekTo(safePos)
        updateSync(safePos)
        if (!_uiState.value.isPlaying) playAudio()
    }

    fun seekForward() {
        if (_uiState.value.showOptions || _uiState.value.showFinalVoteDialog) return
        val safePos =
            minOf(audioPlayerManager.currentPosition + 15000, _uiState.value.nodeEndTimeMs)
        audioPlayerManager.seekTo(safePos)
        updateSync(safePos)
        if (!_uiState.value.isPlaying) playAudio()
    }

    fun selectOption(nextNodeId: String) {
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

    override fun onCleared() {
        super.onCleared()
        audioPlayerManager.release()
    }
}
package com.swyp4.team2.ui.scenario

import android.content.Context
import retrofit2.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.OptIn
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.swyp4.team2.BuildConfig
import com.swyp4.team2.data.local.TokenManager
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
    val pastScripts: List<ScenarioScriptUiModel> = emptyList(),
    val scripts: List<ScenarioScriptUiModel> = emptyList(),
    val activeIndex: Int = -1,
    val maxRevealedIndex: Int = -1,
    val nodeEndTimeMs: Long = 0L,
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
    private val tokenManager: TokenManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScenarioUiState())
    val uiState: StateFlow<ScenarioUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var fullScenario: ScenarioUiModel? = null

    private val player: ExoPlayer = createExoPlayerWithToken()
    private var currentAudioKey: String? = null

    @OptIn(UnstableApi::class)
    private fun createExoPlayerWithToken(): ExoPlayer {
        val accessToken = tokenManager.getAccessToken() ?: ""
        val dataSourceFactory = DefaultHttpDataSource.Factory()
            .setDefaultRequestProperties(mapOf("Authorization" to "Bearer $accessToken"))
        val mediaSourceFactory = DefaultMediaSourceFactory(context)
            .setDataSourceFactory(dataSourceFactory)

        return ExoPlayer.Builder(context)
            .setMediaSourceFactory(mediaSourceFactory)
            .build()
            .apply {
                addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        if (playbackState == Player.STATE_ENDED) {
                            handleNodeEnd()
                        }
                    }
                    override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                        Log.e("ScenarioFlow", "ExoPlayer 에러 발생: ${error.message}")
                    }
                })
            }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun loadScenario(battleId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(pastScripts = emptyList()) }
            scenarioRepository.fetchBattleScenario(battleId)
                .onSuccess { board ->
                    fullScenario = board.toUiModel()
                    val firstNodeId = fullScenario?.startNodeId ?: return@onSuccess
                    loadNode(firstNodeId)
                }
                .onFailure { error ->
                    Log.e("ScenarioFlow", "API 호출 실패: ${error.message}")
                }
        }
    }

    private fun loadNode(nodeId: String) {
        val node = fullScenario?.nodes?.get(nodeId) ?: return

        val targetKey = when {
            node.nodeName.contains("_A") -> "PATH_A"
            node.nodeName.contains("_B") -> "PATH_B"
            else -> currentAudioKey ?: fullScenario?.audios?.keys?.firstOrNull()
        }

        if (targetKey != null && targetKey != currentAudioKey) {
            val audioUrl = fullScenario?.audios?.get(targetKey)
            if (audioUrl != null) {
                currentAudioKey = targetKey
                val baseUrl = BuildConfig.BASE_URL
                val fullAudioUrl = if (audioUrl.startsWith("http")) audioUrl else baseUrl + audioUrl

                player.setMediaItem(MediaItem.fromUri(fullAudioUrl))
                player.prepare()

                if (_uiState.value.currentPositionMs > 0) {
                    player.seekTo(_uiState.value.currentPositionMs)
                }
            }
        }

        val startMs = node.scripts.firstOrNull()?.startTimeMs ?: _uiState.value.currentPositionMs
        val endMs = startMs + (node.audioDuration * 1000L)

        val splitScripts = splitScriptsBySentence(node.scripts, endMs)

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
        val playerTotal = player.duration.let { if (it < 0) 0L else it }

        val scripts = _uiState.value.scripts
        val newActiveIndex = scripts.indexOfLast { it.startTimeMs <= positionMs }
        val newMaxRevealed = maxOf(_uiState.value.maxRevealedIndex, newActiveIndex)

        val isNodeEnded = positionMs >= _uiState.value.nodeEndTimeMs

        _uiState.update { it.copy(
            currentPositionMs = positionMs,
            totalDurationMs = playerTotal,
            activeIndex = newActiveIndex,
            maxRevealedIndex = newMaxRevealed,
            showOptions = isNodeEnded
        )}

        if (isNodeEnded && _uiState.value.isPlaying) {
            handleNodeEnd()
        }
    }

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

    fun seekRewind() {
        val newPos = maxOf(0, player.currentPosition - 15000)
        player.seekTo(newPos)
        updateSync(newPos)
        if (!_uiState.value.isPlaying) {
            playAudio()
        }
    }

    fun seekToPosition(ratio: Float) {
        val newPos = (_uiState.value.totalDurationMs * ratio).toLong()
        val safePos = minOf(newPos, _uiState.value.nodeEndTimeMs)

        player.seekTo(safePos)
        updateSync(safePos)

        if (!_uiState.value.isPlaying) {
            playAudio()
        }
    }

    fun seekForward() {
        val newPos = minOf(player.duration, player.currentPosition + 15000)
        player.seekTo(newPos)
        updateSync(newPos)
        if (!_uiState.value.isPlaying) {
            playAudio()
        }
    }

    fun selectOption(nextNodeId: String) {
        loadNode(nextNodeId)
        playAudio()
    }

    private fun splitScriptsBySentence(
        originalScripts: List<ScenarioScriptUiModel>,
        totalNodeDurationMs: Long
    ): List<ScenarioScriptUiModel> {
        val result = mutableListOf<ScenarioScriptUiModel>()

        for (i in originalScripts.indices) {
            val current = originalScripts[i]

            val nextTimeMs = if (i < originalScripts.lastIndex) {
                originalScripts[i + 1].startTimeMs
            } else {
                totalNodeDurationMs
            }

            val sentences = current.displayText
                .split(Regex("(?<=[.!?])\\s+"))
                .map { it.trim() }
                .filter { it.isNotEmpty() }

            if (sentences.isEmpty()) continue

            val totalChars = sentences.sumOf { it.length }
            val blockDuration = maxOf(0L, nextTimeMs - current.startTimeMs)

            var accumulatedTime = current.startTimeMs

            sentences.forEach { sentence ->
                result.add(
                    current.copy(
                        startTimeMs = accumulatedTime,
                        displayText = sentence
                    )
                )

                val durationForThisSentence = if (totalChars > 0) {
                    (blockDuration * sentence.length) / totalChars
                } else {
                    0L
                }
                accumulatedTime += durationForThisSentence
            }
        }
        return result
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}
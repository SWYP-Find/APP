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
    val showFinalVoteDialog: Boolean = false
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

    // 인증 토큰이 포함된 HTTP 헤더를 설정하여 ExoPlayer 인스턴스를 생성
    @OptIn(UnstableApi::class)
    private fun createExoPlayerWithToken(): ExoPlayer {
        Log.d("TTSFlow", "▶️ createExoPlayerWithToken() 실행 - ExoPlayer 생성 시작")
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
                        Log.d("TTSFlow", "▶️ onPlaybackStateChanged() 실행 - 상태 변경됨: $playbackState")
                        if (playbackState == Player.STATE_ENDED) {
                            handleNodeEnd()
                        }
                    }
                    override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                        Log.e("TTSFlow", "🚨 ExoPlayer 에러 발생: ${error.message}")
                    }
                })
            }
    }

    // 특정 배틀 ID의 시나리오 데이터를 서버에서 가져오고 첫 번째 노드를 실행
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

    // 특정 노드(챕터)를 로드하고, 오디오 준비 및 이전 대화 이력을 관리
    private fun loadNode(nodeId: String) {
        Log.d("TTSFlow", "▶️ loadNode() 실행 - 타겟 nodeId: $nodeId")
        val node = fullScenario?.nodes?.get(nodeId) ?: return

        val targetKey = when {
            node.nodeName.contains("_A") -> "PATH_A"
            node.nodeName.contains("_B") -> "PATH_B"
            else -> currentAudioKey ?: fullScenario?.audios?.keys?.firstOrNull()
        }

        Log.d("TTSFlow", "▶️ loadNode() 판단 - 타겟 오디오 키: $targetKey, 현재 오디오 키: $currentAudioKey")

        if (targetKey != null && targetKey != currentAudioKey) {
            Log.d("TTSFlow", "▶️ loadNode() - 새로운 오디오 파일 로드 및 준비")
            val audioUrl = fullScenario?.audios?.get(targetKey)
            if (audioUrl != null) {
                currentAudioKey = targetKey
                val baseUrl = BuildConfig.BASE_URL
                val fullAudioUrl = if (audioUrl.startsWith("http")) audioUrl else baseUrl + audioUrl

                player.setMediaItem(MediaItem.fromUri(fullAudioUrl))
                player.prepare()

                if (_uiState.value.currentPositionMs > 0) {
                    Log.d("TTSFlow", "▶️ loadNode() - 과거 시간(${_uiState.value.currentPositionMs}ms)으로 이동 (SeekTo)")
                    player.seekTo(_uiState.value.currentPositionMs)
                }
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

    // 재생 위치(ms)에 따라 활성화된 스크립트 인덱스와 진행 상태를 UI에 동기화
    private fun updateSync(positionMs: Long) {
        val playerTotal = player.duration.let { if (it < 0) 0L else it }

        val scripts = _uiState.value.scripts
        val newActiveIndex = scripts.indexOfLast { it.startTimeMs <= positionMs }
        val newMaxRevealed = maxOf(_uiState.value.maxRevealedIndex, newActiveIndex)
        val newMaxListened = maxOf(_uiState.value.maxListenedPositionMs, positionMs)

        val isNodeEnded = positionMs >= _uiState.value.nodeEndTimeMs

        _uiState.update { it.copy(
            currentPositionMs = positionMs,
            maxListenedPositionMs = newMaxListened,
            totalDurationMs = playerTotal,
            activeIndex = newActiveIndex,
            maxRevealedIndex = newMaxRevealed,
            showOptions = isNodeEnded
        )}

        if (isNodeEnded && _uiState.value.isPlaying) {
            handleNodeEnd()
        }
    }

    // 노드 재생이 끝났을 때 자동 다음 노드 이동 또는 사용자 선택지를 노출
    private fun handleNodeEnd() {
        Log.d("TTSFlow", "▶️ handleNodeEnd() 실행 - 노드 끝 도달")
        pauseAudio()
        val currentNode = fullScenario?.nodes?.get(_uiState.value.currentNodeId) ?: return

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

    // 다이얼로그 닫기 (최종 투표로 넘어갈 때 사용)
    fun dismissFinalDialog() {
        _uiState.update { it.copy(showFinalVoteDialog = false) }
    }

    // UI는 그대로 두고 오디오만 처음부터 다시 재생
    fun restartCurrentAudio() {
        Log.d("TTSFlow", "▶️ restartCurrentAudio() 실행 - 오디오 처음부터 다시 재생")
        _uiState.update { it.copy(showFinalVoteDialog = false) } // 다이얼로그 닫기
        player.seekTo(0) // 재생 위치를 맨 처음(0ms)으로 이동
        updateSync(0)    // UI 상태(하이라이트 등) 동기화
        playAudio()      // 다시 재생 시작
    }

    // 현재 재생 상태에 따라 오디오를 재생하거나 일시정지
    fun togglePlayPause() {
        Log.d("TTSFlow", "▶️ togglePlayPause() 실행 - 현재 isPlaying: ${_uiState.value.isPlaying}")
        if (_uiState.value.isPlaying) pauseAudio() else playAudio()
    }

    // 오디오 재생을 시작하고 실시간 UI 동기화를 위한 타이머 작업을 가동
    private fun playAudio() {
        Log.d("TTSFlow", "▶️ playAudio() 실행 - 오디오 재생 시작")
        _uiState.update { it.copy(isPlaying = true) }
        player.play()

        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(100)
                updateSync(player.currentPosition)
            }
        }
    }

    // 오디오를 일시정지하고 실행 중인 동기화 타이머를 취소
    private fun pauseAudio() {
        Log.d("TTSFlow", "▶️ pauseAudio() 실행 - 오디오 일시정지")
        _uiState.update { it.copy(isPlaying = false) }
        player.pause()
        timerJob?.cancel()
    }

    // 오디오를 15초 뒤로 되감고 바로 재생을 시작
    fun seekRewind() {
        Log.d("TTSFlow", "▶️ seekRewind() 실행 - 15초 뒤로 가기")
        val newPos = maxOf(0, player.currentPosition - 15000)
        player.seekTo(newPos)
        updateSync(newPos)

        if (!_uiState.value.isPlaying) {
            playAudio()
        }
    }

    // 특정 비율(0.0~1.0) 위치로 오디오를 이동시키고 바로 재생을 시작
    fun seekToPosition(ratio: Float) {
        Log.d("TTSFlow", "▶️ seekToPosition() 실행 - 요청 비율: $ratio")
        val newPos = (_uiState.value.totalDurationMs * ratio).toLong()
        val safePos = minOf(newPos, _uiState.value.nodeEndTimeMs, _uiState.value.maxListenedPositionMs)

        player.seekTo(safePos)
        updateSync(safePos)

        if (!_uiState.value.isPlaying) {
            playAudio()
        }
    }

    // 오디오를 15초 앞으로 건너뛰고 바로 재생을 시작
    fun seekForward() {
        Log.d("TTSFlow", "▶️ seekForward() 실행 - 15초 앞으로 가기")

        val newPos = minOf(player.duration, player.currentPosition + 15000)
        player.seekTo(newPos)
        updateSync(newPos)

        if (!_uiState.value.isPlaying) {
            playAudio()
        }

        /*// 1. 현재 위치에서 15초 뒤의 목표 지점 계산
        val targetPos = player.currentPosition + 15000

        // 2. 오디오 전체 길이, 현재 챕터(노드)의 끝, 그리고 내가 들었던 최대 시간(maxListened) 중 가장 작은 값을 찾음.
        // 이것이 절대로 넘을 수 없는 "최종 벽"입니다.
        val absoluteMaxPos = minOf(
            player.duration,
            _uiState.value.nodeEndTimeMs,
            _uiState.value.maxListenedPositionMs
        )

        // 3. 목표 지점과 최종 벽 중 더 작은 곳까지만 이동
        val safePos = minOf(targetPos, absoluteMaxPos)

        // 4. 안전한 위치로 점프!
        player.seekTo(safePos)
        updateSync(safePos)

        if (!_uiState.value.isPlaying) {
            playAudio()
        }*/
    }

    // 사용자가 선택한 답변(nextNodeId)을 처리하고 다음 노드를 재생
    fun selectOption(nextNodeId: String) {
        Log.d("TTSFlow", "▶️ selectOption() 실행 - 사용자가 옵션 선택: $nextNodeId")
        val currentOptions = _uiState.value.interactiveOptions
        val totalScriptsSize = _uiState.value.pastScripts.size + _uiState.value.scripts.size

        val newChoice = PastChoice(
            scriptIndex = totalScriptsSize - 1,
            options = currentOptions,
            selectedNextNodeId = nextNodeId
        )

        _uiState.update { it.copy(
            pastChoices = it.pastChoices + newChoice
        )}

        loadNode(nextNodeId)
        playAudio()
    }

    // 긴 스크립트를 문장 단위로 쪼개고 글자 수에 비례한 가짜 시작 시간을 부여
    private fun splitScriptsBySentence(
        originalScripts: List<ScenarioScriptUiModel>,
        totalNodeDurationMs: Long
    ): List<ScenarioScriptUiModel> {
        Log.d("TTSFlow", "▶️ splitScriptsBySentence() 실행 - 스크립트를 문장 단위로 분할")
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

    // 뷰모델 소멸 시 플레이어 리소스를 해제하여 메모리 누수를 방지
    override fun onCleared() {
        Log.d("TTSFlow", "▶️ onCleared() 실행 - 뷰모델 소멸 및 플레이어 해제")
        super.onCleared()
        player.release()
    }
}
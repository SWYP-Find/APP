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
    private val tokenManager: TokenManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScenarioUiState())
    val uiState: StateFlow<ScenarioUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var fullScenario: ScenarioUiModel? = null

    private val player: ExoPlayer = createExoPlayerWithToken()

    @OptIn(UnstableApi::class)
    private fun createExoPlayerWithToken(): ExoPlayer {
        Log.d("ExoPlayerFlow", "🛠️ 1. ExoPlayer 공장 가동: 플레이어 생성 시작")

        // 1. 토큰 가져오기
        val accessToken = tokenManager.getAccessToken() ?: ""
        // 보안상 토큰 전체를 찍지 않고 앞 10자리만 확인합니다.
        val maskedToken = if (accessToken.length > 10) "${accessToken.take(10)}..." else "EMPTY"
        Log.d("ExoPlayerFlow", "🎫 2. 로컬에서 토큰 꺼내기 완료 (토큰 앞부분: $maskedToken)")

        // 2. DataSourceFactory 생성 및 헤더 세팅
        Log.d("ExoPlayerFlow", "🌐 3. HTTP 데이터 소스 팩토리 생성 (여기에 토큰을 기본 헤더로 장착합니다)")
        val dataSourceFactory = DefaultHttpDataSource.Factory()
            .setDefaultRequestProperties(
                mapOf(
                    "Authorization" to "Bearer $accessToken"
                )
            )

        // 3. MediaSourceFactory에 연결
        Log.d("ExoPlayerFlow", "🔗 4. 미디어 소스 팩토리에 방금 만든 HTTP 통신 규칙을 연결합니다")
        val mediaSourceFactory = DefaultMediaSourceFactory(context)
            .setDataSourceFactory(dataSourceFactory)

        // 4. ExoPlayer 빌드
        Log.d("ExoPlayerFlow", "🏗️ 5. ExoPlayer 객체 조립 시작")
        return ExoPlayer.Builder(context)
            .setMediaSourceFactory(mediaSourceFactory)
            .build()
            .apply {
                addListener(object : Player.Listener {
                    // 플레이어의 상태가 변할 때마다 로그를 찍어줍니다! (매우 중요)
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        val stateString = when(playbackState) {
                            Player.STATE_IDLE -> "IDLE (대기 중 - 아무것도 안 함)"
                            Player.STATE_BUFFERING -> "BUFFERING (버퍼링 중 - 🚀 서버와 통신하며 데이터를 받아오는 중!)"
                            Player.STATE_READY -> "READY (재생 준비 완료! 소리 나옴)"
                            Player.STATE_ENDED -> "ENDED (재생 끝)"
                            else -> "UNKNOWN ($playbackState)"
                        }
                        Log.d("ExoPlayerFlow", "▶️ 플레이어 상태 변경: $stateString")

                        if (playbackState == Player.STATE_ENDED) {
                            handleNodeEnd()
                        }
                    }

                    // 에러가 났을 때 원인을 더 깊게 파고듭니다.
                    override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                        Log.e("ExoPlayerFlow", "🚨 앗! ExoPlayer 에러 발생: ${error.message}")

                        // Http 에러인 경우 원인을 더 자세히 출력합니다.
                        val cause = error.cause
                        Log.e("ExoPlayerFlow", "🚨 상세 원인(Cause): ${cause?.message}")
                        Log.e("ExoPlayerFlow", "🚨 이 에러는 STATE_BUFFERING 단계에서 서버가 데이터를 주지 않거나 터졌을 때 발생합니다.")
                    }
                })
                Log.d("ExoPlayerFlow", "✅ 6. ExoPlayer 객체 생성 완료! (⚠️ 주의: 아직 서버랑 통신 시작 안 했음)")
            }
    }

    // 1. 오디오 플레이어
    /*private val player = ExoPlayer.Builder(context).build().apply {
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
    }*/

    // 2. 시나리오 데이터 로드
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
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
                    Log.d("ScenarioFlow", "📝 파싱된 응답 데이터: $board") // 파싱된 객체 출력

                    fullScenario = board.toUiModel()
                    val firstNodeId = fullScenario?.startNodeId ?: return@onSuccess
                    loadNode(firstNodeId)
                }
                .onFailure { error ->
                    Log.e("ScenarioFlow", "❌ API 호출 실패...")
                    if (error is HttpException) {
                        val errorCode = error.code()
                        val errorBody = error.response()?.errorBody()?.string()

                        Log.e("ScenarioFlow", "🚨 HTTP 상태 코드: $errorCode")
                        Log.e("ScenarioFlow", "🚨 에러 응답(JSON): $errorBody")
                    } else {
                        Log.e("ScenarioFlow", "🚨 기타 에러 발생: ${error.message}", error)
                    }
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
        /*if (audioUrl != null) {
            val mediaItem = MediaItem.fromUri(audioUrl)
            player.setMediaItem(mediaItem)
            player.prepare()

            playAudio()
        }*/

        if (audioUrl != null) {
            val baseUrl = BuildConfig.BASE_URL

            val fullAudioUrl = if (audioUrl.startsWith("http")) {
                audioUrl
            } else {
                baseUrl + audioUrl
            }

            Log.d("ScenarioFlow", "🎵 재생 시도할 풀 URL: $fullAudioUrl")

            val mediaItem = MediaItem.fromUri(fullAudioUrl)
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
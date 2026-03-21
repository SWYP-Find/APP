package com.swyp4.team2.ui.debate

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DebateViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    // 🌟 1. 파편화되었던 상태들을 'DebateUiState' 하나로 통합합니다!
    private val _uiState = MutableStateFlow(DebateUiState())
    val uiState: StateFlow<DebateUiState> = _uiState.asStateFlow()

    private var player: ExoPlayer? = null

    init {
        // 🌟 2. 뷰모델이 생성될 때 더미 데이터를 State에 밀어 넣습니다.
        // (나중에는 여기서 API 통신(GET)을 호출해서 받아온 데이터를 넣어주면 됩니다!)
        _uiState.update { currentState ->
            currentState.copy(
                scripts = DebateDummyData.debateScripts,
                interactiveOptions = DebateDummyData.dummyOptions // 선택지도 미리 넣어둠
            )
        }
        initializePlayer()
    }

    private fun initializePlayer() {
        player = ExoPlayer.Builder(context).build().apply {
            // TODO: 나중에 API에서 받은 음성 URL로 교체!
            val mediaItem = MediaItem.fromUri("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
            setMediaItem(mediaItem)
            prepare()

            addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    // 상태 업데이트
                    _uiState.update { it.copy(isPlaying = isPlaying) }
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_READY) {
                        // 곡이 준비되면 총 길이를 가져옵니다. (ExoPlayer는 Long이지만 State는 Int로 맞춤)
                        _uiState.update { it.copy(totalDurationMs = duration.coerceAtLeast(0L).toInt()) }
                    }
                }
            })
            startTrackingProgress()
        }
    }

    private fun startTrackingProgress() {
        viewModelScope.launch {
            while (isActive) {
                player?.let { exo ->
                    if (exo.isPlaying) {
                        val currentPos = exo.currentPosition.toInt()
                        val scripts = _uiState.value.scripts

                        // 🌟 3. timeMs -> startTimeMs 로 모델에 맞게 이름 변경!
                        val newIndex = scripts.indexOfLast { it.startTimeMs <= currentPos }

                        // 상태 업데이트 로직 (현재 시간과 활성화된 말풍선 인덱스를 동시 업데이트)
                        _uiState.update { state ->
                            state.copy(
                                currentPositionMs = currentPos,
                                activeIndex = if (newIndex != -1) newIndex else state.activeIndex
                            )
                        }
                    }
                }
                delay(100L) // 0.1초마다 싱크 체크
            }
        }
    }

    fun togglePlayPause() {
        player?.let { if (it.isPlaying) it.pause() else it.play() }
    }

    fun seekForward() {
        player?.let { it.seekTo(it.currentPosition + 15000L) }
    }

    fun seekRewind() {
        player?.let { it.seekTo((it.currentPosition - 15000L).coerceAtLeast(0L)) }
    }

    fun seekToPosition(ratio: Float) {
        player?.let {
            val targetPosition = (ratio * _uiState.value.totalDurationMs).toLong()
            it.seekTo(targetPosition)
            _uiState.update { state -> state.copy(currentPositionMs = targetPosition.toInt()) }
        }
    }

    // 🌟 4. 유저가 선택지를 클릭했을 때 호출될 함수 (미리 만들어둠!)
    fun selectOption(nextNodeId: String) {
        // TODO: 나중에 여기서 API(POST 등)를 호출하여 유저의 선택을 서버에 보냅니다.
        // 호출 성공 후, 새로운 스크립트(nextNodeId에 해당하는)와 음성을 받아와서 uiState와 Player를 업데이트하면 됩니다!
        println("선택된 노드 ID: $nextNodeId")
    }

    override fun onCleared() {
        super.onCleared()
        player?.release()
        player = null
    }
}
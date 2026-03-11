package com.swyp4.team2.ui.debate

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.swyp4.team2.domain.model.DebateMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.media3.common.Player

@HiltViewModel
class DebateViewModel @Inject constructor(
    @ApplicationContext private val context: Context
): ViewModel(){
    private val _scripts = MutableStateFlow<List<DebateMessage>>(DebateDummyData.debateScripts)
    val scripts = _scripts.asStateFlow()

    private val _activeIndex = MutableStateFlow(-1)
    val activeIndex = _activeIndex.asStateFlow()

    // 오디오 관련 상태들
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    private val _currentPositionMs = MutableStateFlow(0L)
    val currentPositionMs = _currentPositionMs.asStateFlow()

    private val _totalDurationMs = MutableStateFlow(0L)
    val totalDurationMs = _totalDurationMs.asStateFlow()

    private var player: ExoPlayer? = null

    init{
        initializePlayer()
    }

    private fun initializePlayer(){
        player = ExoPlayer.Builder(context).build().apply{
            val mediaItem = MediaItem.fromUri("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
            setMediaItem(mediaItem)
            prepare()

            addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    _isPlaying.value = isPlaying
                }
                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_READY) {
                        // 곡이 준비되면 총길이를 가져옵니다. (음수 방지를 위해 최소 0L로 세팅)
                        _totalDurationMs.value = duration.coerceAtLeast(0L)
                    }
                }
            })
            startTrackingProgress()
        }
    }

    private fun startTrackingProgress(){
        viewModelScope.launch {
            while(isActive){
                player?.let { exo ->
                    if(exo.isPlaying) {
                        // 1. 현재 재생 시간 UI 상태 업데이트
                        val currentPos = exo.currentPosition
                        _currentPositionMs.value = currentPos

                        // 2. 아까 만들었던 대본 싱크 로직! (현재 시간에 맞는 말풍선 찾기)
                        val newIndex = _scripts.value.indexOfLast { it.startTimeMs <= currentPos }
                        if(newIndex != -1 && newIndex != _activeIndex.value){
                            _activeIndex.value = newIndex
                        }
                    }
                }
                delay(100L) // 0.1초 대기 (while문 안에 안전하게 위치!)
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
        player?.let { it.seekTo((it.currentPosition - 15000L).coerceAtLeast(0L)) } // 0초 밑으로 안 내려가게 방어!
    }

    fun seekToPosition(ratio: Float) {
        player?.let {
            val targetPosition = (ratio * _totalDurationMs.value).toLong()
            it.seekTo(targetPosition)
            _currentPositionMs.value = targetPosition
        }
    }

    override fun onCleared() {
        super.onCleared()
        player?.release()
        player = null
    }
}
package com.picke.app.ui.scenario

import android.content.Context
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.picke.app.data.local.TokenManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@androidx.annotation.OptIn(UnstableApi::class)
class AudioPlayerManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val tokenManager: TokenManager
) {
    companion object {
        private const val TAG = "AudioPlayerManager"
    }

    private val player: ExoPlayer = createPlayer()

    val currentPosition: Long get() = player.currentPosition
    val duration: Long get() = if (player.duration < 0) 0L else player.duration

    var onPlaybackEnded: (() -> Unit)? = null

    private fun createPlayer(): ExoPlayer {
        Log.d(TAG, "ExoPlayer 생성 시작")
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
                            Log.d(TAG, "재생 완료 → onPlaybackEnded 호출")
                            onPlaybackEnded?.invoke()
                        }
                    }
                    override fun onPlayerError(error: PlaybackException) {
                        Log.e(TAG, "재생 에러: ${error.message}")
                    }
                })
            }
    }

    fun loadAudio(url: String, seekToMs: Long = 0) {
        Log.d(TAG, "오디오 로드 - url: $url, seekTo: ${seekToMs}ms")
        player.setMediaItem(MediaItem.fromUri(url))
        player.prepare()
        if (seekToMs > 0) player.seekTo(seekToMs)
    }

    fun play() { player.play() }
    fun pause() { player.pause() }
    fun seekTo(positionMs: Long) { player.seekTo(positionMs) }

    fun release() {
        Log.d(TAG, "ExoPlayer 해제")
        player.release()
    }
}
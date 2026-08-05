package com.picke.presentation.ui.scenario

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import javax.inject.Inject

@androidx.annotation.OptIn(UnstableApi::class)
class AudioPlayerManager @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val okHttpClient: OkHttpClient
) {

    private val player: ExoPlayer = createPlayer()

    val currentPosition get() = player.currentPosition
    val duration get() = if (player.duration < 0) 0L else player.duration

    var onPlaybackEnded: (() -> Unit)? = null

    private fun createPlayer(): ExoPlayer {
        val dataSourceFactory = OkHttpDataSource.Factory(okHttpClient)
        val mediaSourceFactory =
            DefaultMediaSourceFactory(context).setDataSourceFactory(dataSourceFactory)

        return ExoPlayer.Builder(context)
            .setMediaSourceFactory(mediaSourceFactory)
            .build()
            .apply {
                addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        if (playbackState == Player.STATE_ENDED) {
                            onPlaybackEnded?.invoke()
                        }
                    }

                    override fun onPlayerError(error: PlaybackException) {
                        //
                    }
                })
            }
    }

    fun loadAudio(url: String, seekToMs: Long = 0) {
        player.setMediaItem(MediaItem.fromUri(url))
        player.prepare()
        if (seekToMs > 0) player.seekTo(seekToMs)
    }

    fun play() {
        player.play()
    }

    fun pause() {
        player.pause()
    }

    fun seekTo(positionMs: Long) {
        player.seekTo(positionMs)
    }

    fun release() {
        player.release()
    }
}
package com.bash.composemedia3.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.bash.composemedia3.data.User

@SuppressLint("UnsafeOptInUsageError")
@Composable
fun PlayerScreen(modifier: Modifier = Modifier, user : User) {

    val context = LocalContext.current

    val  exoPlayer = remember { initPlayer(user,context) }

    val lifecycleOwner = LocalLifecycleOwner.current

    var isPlaying by remember { mutableStateOf(false) }

    DisposableEffect(exoPlayer) {

        val listener  = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
            }

            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
                super.onIsPlayingChanged(playing)
            }
        }

        exoPlayer.addListener(listener)

        onDispose {

            exoPlayer.removeListener(listener)

            exoPlayer.release()

        }
    }

    DisposableEffect(lifecycleOwner) {

        val lifecycleObserver = LifecycleEventObserver({ _, event ->

            when(event) {
                Lifecycle.Event.ON_RESUME->{
                    startPlayback(exoPlayer)
                }

                Lifecycle.Event.ON_STOP ->{
                    if (isPlaying) {
                        exoPlayer.pause()
                    }

                }

                else -> Unit
            }


        })

        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)


        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }

/*
    LifecycleResumeEffect(user) {

        player = initPlayer(user,context)

        onPauseOrDispose {
            player?.apply { release() }
            player = null
        }
    }

    LifecycleStartEffect(user) {
        player = initPlayer(user,context)

        onStopOrDispose {
            player?.apply {
                release()
            }
            player = null
        }


    }
*/

    Box(modifier = modifier.fillMaxSize()) {

        AndroidView(modifier = Modifier.matchParentSize(), factory = {
            PlayerView(context).apply {
                player = exoPlayer
            }
        })
        ListItem({Text(user.name)})

    }



}

internal fun startPlayback(player: Player) {
    if (player.mediaItemCount == 0) {
        player.addMediaItem(MediaItem.fromUri("https://dash.akamaized.net/akamai/bbb_30fps/bbb_30fps.mpd"))
        player.prepare()
        player.play()
    } else {
        player.play()
    }
}

@UnstableApi
internal fun initPlayer(user: User, current: Context) : ExoPlayer {

    val dataSourceFactory = DefaultDataSource.Factory(current)

    val rendererFactory = DefaultRenderersFactory(current).setEnableDecoderFallback(true)


    val player = ExoPlayer.Builder(current,rendererFactory).build().apply {
        setMediaItem(MediaItem.fromUri("https://dash.akamaized.net/akamai/bbb_30fps/bbb_30fps.mpd"))
        prepare()
    }


    return player
}
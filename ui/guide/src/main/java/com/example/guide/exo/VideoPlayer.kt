package com.example.guide.exo

import android.animation.TimeInterpolator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import com.google.android.exoplayer2.C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.REPEAT_MODE_ONE
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ui.StyledPlayerView

class VideoPlayer(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAtr: Int = 0,
) : StyledPlayerView(context, attrs, defStyleAtr) {
    private val fadeOutInterpolator: TimeInterpolator = AccelerateInterpolator()
    private val fadeTime = 1000L
    private var mediaItem: MediaItem? = null
    private val overlayView = View(context).apply {
        setBackgroundColor(Color.YELLOW)
    }

    init {
        useController = false
    }

    fun setVideoUrl(url: String) {
        if (player == null) {
            player = createPlayer()
        }
        mediaItem = MediaItem.fromUri(url).also {
            player?.setMediaItem(it)
        }
    }

    fun stop() {
//        fadeIn()
        player?.stop()
    }

    fun pause() = player?.pause()
    fun play() {
//        fadeOut()
        player?.play()
        val time = player?.currentPosition ?: 0
        player?.seekTo(time + 60000)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (player == null) {
            player = createPlayer()
        }
        mediaItem?.let { player?.setMediaItem(it) }
        player?.prepare()
    }

    override fun onDetachedFromWindow() {
        player?.run {
            stop()
            release()
        }
        player = null
        super.onDetachedFromWindow()
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        if (visibility == VISIBLE) {
            play()
        }
    }

    private fun createPlayer(): Player {
        return ExoPlayer.Builder(context)
            .setVideoScalingMode(VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
            .setAudioAttributes(AudioAttributes.DEFAULT, false)
            .build().apply {
                volume = 0.5f
                repeatMode = REPEAT_MODE_ONE
            }
    }

    fun fadeIn() {
        overlayView.alpha = 0.5f
        overlayView.performFade(
            animate = true,
            endAlpha = 1f,
            timeInterpolator = fadeOutInterpolator,
            animationDuration = fadeTime,
            endAction = {}
        )
    }

    fun fadeOut() {
        overlayView.alpha = 0.5f
        overlayView.performFade(
            animate = true,
            endAlpha = 0f,
            timeInterpolator = fadeOutInterpolator,
            animationDuration = fadeTime,
            endAction = {}
        )
    }
}

fun View.performFade(
    animate: Boolean,
    endAlpha: Float,
    timeInterpolator: TimeInterpolator?,
    animationDuration: Long = 0,
    startDelay: Long = 0,
    endAction: (() -> Unit)? = null
) {
    animate().cancel()
    if (animate) {
        animate()
            .alpha(endAlpha)
            .setDuration(animationDuration)
            .setStartDelay(startDelay)
            .setInterpolator(timeInterpolator)
            .withEndAction { endAction?.invoke() }
    } else {
        alpha = endAlpha
    }
}
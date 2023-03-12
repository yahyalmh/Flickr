package com.example.guide.exo

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.isVisible
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_FILL
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_FIT

@Composable
fun VideoView() {
    val videoUrls = buildList {
        add("https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4")
        add("https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4")
        add("https://storage.googleapis.com/exoplayer-test-media-0/play.mp3")
//        add("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4")
        add("https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4")
        add("https://storage.googleapis.com/exoplayer-test-media-0/play.mp3")
        add("https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4")
        add("https://storage.googleapis.com/exoplayer-test-media-0/play.mp3")
        add("https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4")
        add("https://storage.googleapis.com/exoplayer-test-media-0/play.mp3")
    }
    val itemCount = videoUrls.size - 1
    var activeItem by remember { mutableStateOf(0) }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .background(Color.Black)
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(modifier = Modifier.padding(12.dp), text = activeItem.toString(), fontSize = 20.sp)
            OnBoardingVideoContent(videoUrls, activeItem)
        }
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(modifier = Modifier.padding(15.dp),
                onClick = { if (activeItem > 0) activeItem -= 1 }) {
                Text(text = "Previous")
            }
            Button(modifier = Modifier.padding(15.dp),
                onClick = { if (activeItem < itemCount) activeItem += 1 }) {
                Text(text = "Next")
            }
        }
    }

}

@Composable
private fun OnBoardingVideoContent(
    videoUrls: List<String>,
    activeItem: Int,
) {
    val progress = remember(activeItem) { Animatable(0f) }
    val currentVideoUrl = remember { mutableStateOf(false) }

    LaunchedEffect(activeItem) {
        if (activeItem > 0) {
            progress.animateTo(targetValue = 1f, animationSpec = tween(1000))
        }
//        currentVideoUrl.value = true
        progress.animateTo(targetValue = 0f, animationSpec = tween(1000))
    }
    Box(modifier = Modifier.fillMaxSize()) {
        repeat(videoUrls.size) { index ->
            VideoItem2(videoUrl = videoUrls[activeItem], index == activeItem )
        }
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Green.copy(alpha = progress.value))
        )
    }
}

@Composable
private fun VideoItem2(videoUrl: String, isActive: Boolean) {
    AnimatedVisibility(visible = isActive) {

        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    VideoPlayer(context).apply {
                        resizeMode = RESIZE_MODE_FILL
                        setVideoUrl(videoUrl)
                    }
                }, update = {
                    it.isVisible = isActive
                    when (isActive) {
                        true -> it.play()
                        false -> it.stop()
                    }
                })
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun VideoPreview() {
    VideoView()
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun VideoItem(videoUrl: String, isActive: Boolean) {
    AnimatedContent(
        targetState = videoUrl,
        transitionSpec = { fadeIn() with fadeOut() }) {
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(modifier = Modifier.fillMaxSize(), factory = { context ->
                VideoPlayer(context).apply {
                    setVideoUrl(it)
                    resizeMode = RESIZE_MODE_FIT
                }
            }, update = {
//                    when (isActive) {
//                        true -> it.play()
//                        false -> it.stop()
//                    }
            })
        }
    }
}
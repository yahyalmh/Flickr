package com.example.guide

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlin.math.roundToInt

@Composable
fun SegmentBar(
    modifier: Modifier = Modifier,
    segmentsCount: Int,
    color: Color = Color.White,
    backgroundOpacity: Float = 0.5f,
    backgroundColor: Color = color.copy(alpha = backgroundOpacity),
    durationMillisPerSegment: Int = 1000,
    state: SegmentBarState = rememberSegmentBarState(),
    onBarFinish: () -> Unit = {},
) {
    if (state.isVisible) {
        state.segmentsCount = segmentsCount - 1
        if (state.isFinished) {
            onBarFinish()
        }
        Row(
            modifier = modifier
                .zIndex(1f)
                .background(
                    brush = verticalGradient(
                        listOf(Color.LightGray.copy(alpha = 0.6f), Color.Transparent)
                    )
                )
                .fillMaxWidth()
                .padding(vertical = 3.dp, horizontal = 2.dp)
                .height(10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            repeat(segmentsCount) {
                Segment(
                    modifier = Modifier.weight(1f),
                    id = it,
                    color = color,
                    backgroundColor = backgroundColor,
                    state = state,
                    animationDuration = durationMillisPerSegment
                )
            }
        }
    }
}

@Composable
internal fun Segment(
    modifier: Modifier,
    id: Int,
    color: Color,
    backgroundColor: Color,
    height: Dp = 4.dp,
    animationDuration: Int = 1000,
    state: SegmentBarState,
) {
    val progress = animatable(
        state = state,
        id = id,
        animationDuration = animationDuration,
    )
    LinearProgressIndicator(
        modifier = modifier
            .padding(1.5.dp)
            .height(height)
            .background(Color.Transparent)
            .zIndex(1f)
            .border(0.1.dp, Color.LightGray, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth(),
        color = color,
        backgroundColor = backgroundColor,
        progress = progress
    )
}

@Composable
private fun animatable(
    state: SegmentBarState,
    id: Int,
    animationDuration: Int,
): Float {
    val action = state.getCurrentActionType(id)
    val targetValue = 1f
    val progress = remember { Animatable(state.getProgressStartPoint(id)) }
    if (action == ActionType.RUN && progress.value == targetValue) {
        onNextSegment(state)
    }
    LaunchedEffect(progress.value) { state.saveProgress(id, progress.value) }
    LaunchedEffect(action) {
        when (action) {
            ActionType.RUN -> {
                val duration = animationDuration * (targetValue - progress.value)
                progress.animateTo(
                    targetValue = targetValue,
                    animationSpec = tween(duration.roundToInt(), easing = LinearEasing)
                )
            }
            ActionType.NEXT -> {
                progress.snapTo(targetValue)
                onNextSegment(state)
            }
            ActionType.PREVIOUS -> {
                progress.snapTo(0f)
                state.moveToPreviousSegment()
            }
            ActionType.STOP -> state.saveProgress(id, progress.value)
            ActionType.RESET -> progress.snapTo(0f)
            ActionType.NONE -> Unit
        }
    }
    return progress.value
}

private fun onNextSegment(
    state: SegmentBarState,
) = if (state.currentSegmentIndex == state.segmentsCount) {
    state.finish()
} else {
    state.moveToNextSegment()
}

@Composable
@Preview(showBackground = true, heightDp = 200)
fun SegmentBarPreview() {
    SegmentBar(segmentsCount = 3, backgroundColor = Color.Black)
}

package com.example.guide

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
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
    color: Color = Color.LightGray,
    backgroundOpacity: Float = 0.25f,
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
                .padding(vertical = 3.dp, horizontal = 2.dp)
                .zIndex(1f)
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
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
    height: Dp = 3.dp,
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
            .border(0.001.dp, Color.LightGray, RoundedCornerShape(16.dp))
//            .shadow(elevation = 1.dp, spotColor = Color.Transparent)
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth(),
        color = color,
        backgroundColor = backgroundColor,
        progress = progress.value
    )
}

@Composable
private fun animatable(
    state: SegmentBarState,
    id: Int,
    animationDuration: Int,
): Animatable<Float, AnimationVector1D> {
    val action = state.getCurrentActionType(id)
    val targetValue = 1f
    val progress = remember { Animatable(state.getProgressStartPoint(id)) }
    if (action == ActionType.RUN && progress.value == targetValue) {
        onNextSpot(state)
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
                onNextSpot(state)
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
    return progress
}

private fun onNextSpot(
    state: SegmentBarState,
) = if (state.currentSegmentIndex == state.segmentsCount) {
    state.finish()
} else {
    state.moveToNextSegment()
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun SegmentBarPreview() {
    SegmentBar(segmentsCount = 6)
}

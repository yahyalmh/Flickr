package com.example.ui.common.component.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.ui.common.test.TestTag
import kotlinx.coroutines.delay

/**
 * @author yaya (@yahyalmh)
 * @since 10th November 2022
 */

@Composable
fun LoadingView(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
) {
    AnimatedVisibility(
        modifier = modifier.testTag(TestTag.LOADING),
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Column(
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@Composable
fun ThreeCircleLoadingView(
    modifier: Modifier = Modifier,
    isVisible: Boolean = false,
    message: String? = null,
    circleSize: Dp = 12.dp,
    circleColor: Color = MaterialTheme.colorScheme.primary,
    spaceBetween: Dp = 8.dp,
    travelDistance: Dp = 16.dp
) {
    AnimatedVisibility(
        modifier = modifier
            .zIndex(1f)
            .testTag(TestTag.LOADING),
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        val circles = listOf(
            remember { Animatable(initialValue = 0f) },
            remember { Animatable(initialValue = 0f) },
            remember { Animatable(initialValue = 0f) },
        )
        val circleValues = circles.map { it.value }
        val distancePx = with(LocalDensity.current) { travelDistance.toPx() }

        circles.forEachIndexed { index, animatable ->
            LaunchedEffect(key1 = animatable) {
                delay(index * 100L)
                animatable.animateTo(1f,
                    animationSpec = infiniteRepeatable(
                        animation = keyframes {
                            durationMillis = 1200
                            0f at 0 with LinearOutSlowInEasing
                            1f at 300 with LinearOutSlowInEasing
                            0f at 600 with LinearOutSlowInEasing
                            0f at 1200 with LinearOutSlowInEasing
                        }
                    )
                )
            }
        }

        Column(
            modifier = modifier
                .background(Color.Transparent)
                .padding(10.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = modifier
                    .wrapContentSize()
                    .background(
                        Color.Transparent.copy(alpha = 0.2f),
                        RoundedCornerShape(14)
                    )
                    .padding(start = 12.dp, end = 12.dp, bottom = 12.dp, top = 14.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = modifier.padding(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        spaceBetween,
                        alignment = Alignment.CenterHorizontally
                    )
                ) {
                    circleValues.forEach { value ->
                        Box(modifier = modifier
                            .size(circleSize)
                            .graphicsLayer { translationY = -value * distancePx }
                            .background(circleColor, shape = CircleShape))
                    }
                }
                message?.let {
                    Spacer(modifier = modifier.height(3.dp))
                    Text(
                        modifier = modifier.padding(4.dp),
                        textAlign = TextAlign.Center,
                        text = it,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ThreeCircleLoadingPreview() {
    ThreeCircleLoadingView(isVisible = true, message = "Loading")
}

@Preview(showBackground = true)
@Composable
fun LoadingPreview() {
    LoadingView(isVisible = true)
}

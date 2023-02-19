package com.example.guide

import android.view.ViewConfiguration
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.AndroidViewConfiguration
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.ui.common.component.icon.AppIcons
import com.example.ui.common.component.view.AutoRetryView
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SegmentScreen(
    modifier: Modifier = Modifier,
    content: List<@Composable () -> Unit>,
    durationMillisPerSegment: Int = 1000,
    onFinish: () -> Unit,
) {
    val viewConfiguration = AndroidViewConfiguration(ViewConfiguration.get(LocalContext.current))
    val screenWidthHalf = (LocalConfiguration.current.screenWidthDp / 2f)
    val segmentBarState = rememberSegmentBarState()
    Box(
        modifier = modifier
            .padding(2.dp)
            .fillMaxSize()
            .pointerInput(Unit) {
                handleTaps(
                    viewConfiguration = viewConfiguration,
                    halfScreenWidth = screenWidthHalf,
                    segmentBarState = segmentBarState
                )
            },
        contentAlignment = Alignment.Center
    ) {
        SegmentBar(
            modifier = Modifier.align(Alignment.TopCenter),
            segmentsCount = content.size,
            durationMillisPerSegment = durationMillisPerSegment,
            state = segmentBarState,
            onBarFinish = onFinish
        )

        AnimatedContent(
            modifier = Modifier.zIndex(-1f),
            targetState = segmentBarState.currentSegmentIndex
        ) {
            content[segmentBarState.currentSegmentIndex]()
        }
    }
}

private suspend fun PointerInputScope.handleTaps(
    viewConfiguration: AndroidViewConfiguration,
    halfScreenWidth: Float,
    segmentBarState: SegmentBarState
) = coroutineScope {
    detectTapGestures(
        onPress = {
            try {
                segmentBarState.stop()
                withTimeout(viewConfiguration.longPressTimeoutMillis) {
                    awaitRelease()
                }
                if (it.x.toDp().value >= halfScreenWidth) {
                    segmentBarState.next()
                } else {
                    segmentBarState.previous()
                }
            } catch (e: TimeoutCancellationException) {
                segmentBarState.hide()
                awaitRelease()
                segmentBarState.show()
                segmentBarState.run()
            }
        }
    )
}

@Composable
@Preview(showBackground = true)
fun SegmentScreenPreview() {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val content = buildList<@Composable () -> Unit> {
        repeat(5) {
            add {
                AutoRetryView(
                    icon = AppIcons.Search,
                    errorMessage = "This is page number $it"
                )
            }
        }
    }
    SegmentScreen(durationMillisPerSegment = 1500, content = content) {
        coroutineScope.launch {
            Toast.makeText(context, "Finished", Toast.LENGTH_SHORT).show()
        }
    }
}
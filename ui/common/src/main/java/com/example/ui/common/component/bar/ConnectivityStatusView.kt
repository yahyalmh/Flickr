package com.example.ui.common.component.bar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.flickr.ui.common.R
import com.example.ui.common.test.TestTag
import kotlinx.coroutines.delay

/**
 * @author yaya (@yahyalmh)
 * @since 02th November 2022
 */

@Composable
fun ConnectivityStatusView(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    isOnline: Boolean,
) {
    val hideOnlineViewDelay = 1500L
    val animationDuration = 500
    val backgroundColor by animateColorAsState(if (isOnline) Color.Green else Color.Red)
    val textId = remember(isOnline) { if (isOnline) R.string.online else R.string.waitForNetwork }
    val testTag = if (isOnline) TestTag.ONLINE_STATUS_VIEW else TestTag.OFFLINE_STATUS_VIEW

    var isRemainVisible by remember(isVisible, isOnline) { mutableStateOf(true) }
    LaunchedEffect(isOnline) {
        if (isOnline) {
            delay(hideOnlineViewDelay)
            isRemainVisible = false
        }
    }

    AnimatedVisibility(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(backgroundColor)
            .testTag(testTag),
        visible = isVisible && isRemainVisible,
        enter = fadeIn(animationSpec = tween(animationDuration)) +
                expandVertically(animationSpec = tween(animationDuration)),
        exit = fadeOut(animationSpec = tween(animationDuration)) +
                shrinkVertically(animationSpec = tween(animationDuration))
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = modifier.animateContentSize(),
                text = stringResource(textId),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview
@Composable
fun OnlinePreview() {
    ConnectivityStatusView(isOnline = true, isVisible = true)
}

@Preview
@Composable
fun OfflinePreview() {
    ConnectivityStatusView(isOnline = false, isVisible = true)
}
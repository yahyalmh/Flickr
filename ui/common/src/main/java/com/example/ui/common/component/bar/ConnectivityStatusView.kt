package com.example.ui.common.component.bar

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.ui.common.test.TestTag
import com.example.flickr.ui.common.R

/**
 * @author yaya (@yahyalmh)
 * @since 02th November 2022
 */

@Composable
fun ConnectivityStatusView(
    modifier: Modifier = Modifier,
    isOnlineViewVisible: Boolean,
    isOfflineViewVisible: Boolean,
) {
    OfflineView(modifier = modifier, isVisible = isOfflineViewVisible)
    OnlineView(modifier = modifier, isVisible = isOnlineViewVisible)
}

@Composable
internal fun OfflineView(
    modifier: Modifier = Modifier,
    isVisible: Boolean
) {
    AnimatedVisibility(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.Red)
            .testTag(TestTag.ONLINE_STATUS_VIEW),
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(500)) +
                expandVertically(animationSpec = tween(500)),
        exit = fadeOut(animationSpec = tween(500)) +
                shrinkVertically(animationSpec = tween(500))
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.waitForNetwork),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
internal fun OnlineView(
    modifier: Modifier = Modifier,
    isVisible: Boolean
) {
    AnimatedVisibility(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.Green)
            .testTag(TestTag.OFFLINE_STATUS_VIEW),
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(500)) +
                expandVertically(animationSpec = tween(500)),
        exit = fadeOut(animationSpec = tween(500)) +
                shrinkVertically(animationSpec = tween(500))
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.online),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Preview
@Composable
fun OfflinePreview() {
    ConnectivityStatusView(isOnlineViewVisible = false, isOfflineViewVisible = true)
}

@Preview
@Composable
fun OnlinePreview() {
    ConnectivityStatusView(isOnlineViewVisible = true, isOfflineViewVisible = false)
}
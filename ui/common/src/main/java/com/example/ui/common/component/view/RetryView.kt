package com.example.ui.common.component.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ui.common.component.icon.AppIcons
import com.example.ui.common.test.TestTag
import  com.example.flickr.ui.common.R


/**
 * @author yaya (@yahyalmh)
 * @since 10th November 2022
 */

@Composable
fun RetryView(
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
    retryMessage: String? = null,
    icon: ImageVector, onRetry: () -> Unit
) {
    AnimatedVisibility(
        modifier = modifier.testTag(TestTag.RETRY_VIEW),
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Column(
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(
                space = 30.dp,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = modifier
                    .padding(12.dp)
                    .size(120.dp),
                imageVector = icon,
                contentDescription = stringResource(id = R.string.warningIconDescription),
                tint = MaterialTheme.colorScheme.error
            )

            Text(
                text = retryMessage ?: stringResource(id = R.string.defaultErrorHint),
                modifier = modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )

            OutlinedButton(onClick = onRetry, shape = MaterialTheme.shapes.medium) {
                Text(modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                    text = stringResource(R.string.retry),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RetryPreview() {
    RetryView(icon = AppIcons.Warning) {}
}

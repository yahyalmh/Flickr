package com.example.ui.common.component.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ui.common.component.icon.AppIcons
import com.example.ui.common.test.TestTag
import com.example.flickr.ui.common.R

@Composable
fun AutoRetryView(
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
    errorMessage: String? = null,
    icon: ImageVector,
    hint: String = stringResource(id = R.string.autoRetryHint),
) {
    AnimatedVisibility(
        modifier = modifier.testTag(TestTag.AUTO_RETRY_VIEW),
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
                text = errorMessage ?: stringResource(id = R.string.defaultErrorHint),
                modifier = modifier.padding(12.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )

            Text(
                text = hint,
                modifier = modifier.padding(12.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Green
            )

        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AutoRetryPreview() {
    AutoRetryView(icon = AppIcons.Warning)
}
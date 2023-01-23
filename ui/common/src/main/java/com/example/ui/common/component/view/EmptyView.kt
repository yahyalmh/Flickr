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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.common.component.icon.AppIcons
import com.example.ui.common.test.TestTag
import  com.example.flickr.ui.common.R

@Composable
fun EmptyView(
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
    icon: ImageVector,
    message: String,
) {
    ContentView(
        modifier = modifier,
        isVisible = isVisible,
        icon = icon
    ) {
        Text(
            modifier = Modifier.padding(10.dp),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 16.sp,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center,
            text = message
        )
    }
}

@Composable
fun EmptyView(
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
    icon: ImageVector,
    message: AnnotatedString,
) {
    ContentView(
        modifier = modifier,
        isVisible = isVisible,
        icon = icon
    ) {
        Text(
            modifier = Modifier.padding(10.dp),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            text = message
        )
    }
}

@Composable
private fun ContentView(
    modifier: Modifier,
    isVisible: Boolean,
    icon: ImageVector,
    message: @Composable () -> Unit
) {
    AnimatedVisibility(
        modifier = modifier.testTag(TestTag.EMPTY_VIEW),
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Column(
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                modifier = Modifier
                    .padding(12.dp)
                    .size(80.dp),
                imageVector = icon,
                contentDescription = stringResource(id = R.string.emptyIcon),
                tint = MaterialTheme.colorScheme.onBackground
            )

            message()
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun Preview() {
    EmptyView(isVisible = true, icon = AppIcons.Search, message = "Not Found")
}
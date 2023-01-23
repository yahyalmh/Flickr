package com.example.ui.common.component.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun TwoOptionAlertDialog(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    title: String,
    message: String,
    confirmColor: Color,
    confirmText: String,
    dismissText: String,
    isDismissible: Boolean = true,
    onConfirmListener: () -> Unit,
    onDismissListener: () -> Unit,
) {
    AnimatedVisibility(
        modifier = modifier
            .zIndex(1f)
            .clickable(enabled = isDismissible) { onDismissListener() },
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(150)),
        exit = fadeOut(animationSpec = snap()),
    ) {
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
                    .width(IntrinsicSize.Max)
                    .wrapContentHeight()
                    .background(
                        MaterialTheme.colorScheme.background,
                        RoundedCornerShape(14)
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = modifier.height(8.dp))
                Text(
                    modifier = modifier.padding(vertical = 8.dp, horizontal = 14.dp),
                    textAlign = TextAlign.Center,
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                )
                Text(
                    textAlign = TextAlign.Center,
                    modifier = modifier.padding(vertical = 8.dp, horizontal = 14.dp),
                    text = message,
                    style = MaterialTheme.typography.bodyMedium
                )

                Divider(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .height(1.dp)
                        .fillMaxWidth()
                )

                Row(
                    modifier = modifier
                        .height(IntrinsicSize.Min)
                        .wrapContentWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(
                        modifier = modifier.weight(1f),
                        onClick = { onDismissListener() }) {
                        Text(text = dismissText)
                    }


                    Divider(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp)
                    )

                    TextButton(
                        modifier = modifier.weight(1f),
                        onClick = { onConfirmListener() }) {
                        Text(text = confirmText, color = confirmColor)
                    }

                }
            }
        }
    }
}

@Preview(widthDp = 400)
@Composable
fun TwoOptionDialogPreview() {
    TwoOptionAlertDialog(
        isVisible = true,
        title = "Discard Receipt?",
        message = "If you go back, you lose scanned receipt",
        confirmColor = Color.Red,
        confirmText = "Discard",
        dismissText = "Cancel",
        onConfirmListener = { }) { }
}
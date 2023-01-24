package com.example.ui.common.component.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.data.common.ext.RandomString
import com.example.flickr.ui.common.R
import com.example.ui.common.component.icon.AppIcons

@Composable
fun HistoryCell(
    modifier: Modifier = Modifier,
    history: String,
    onClick: (history: String) -> Unit = {},
    onLeadingIconClick: (history: String) -> Unit
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick(history) }
            .background(MaterialTheme.colorScheme.inverseOnSurface, RoundedCornerShape(15)),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(4.dp)
                .padding(start = 6.dp), text = history
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            modifier = Modifier
                .padding(4.dp)
                .clickable { onLeadingIconClick(history) }
                .size(20.dp),
            imageVector = AppIcons.Close,
            contentDescription = stringResource(id = R.string.search),
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun String.toHistoryCell(
    onClearClick: (history: String) -> Unit,
    onClick: (history: String) -> Unit
): @Composable () -> Unit = {
    HistoryCell(
        history = this,
        onClick = onClick,
        onLeadingIconClick = onClearClick
    )
}


@Composable
@Preview
fun HistoryCellPreview() {
    HistoryCell(
        onClick = {},
        history = RandomString(),
    ) {}
}


package com.example.ui.common.component.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.example.ui.common.component.BaseLazyColumn
import com.example.ui.common.component.bar.SearchBar

@Composable
fun SearchBarScaffold(
    modifier: Modifier = Modifier,
    hint: String,
    savedQuery: String,
    histories: List<String>,
    onQueryChange: (query: String) -> Unit,
    onCancelClick: () -> Unit,
    onSearchClick: (text: String) -> Unit,
    onHistoryClick: (history: String) -> Unit,
    onClearHistoryClick: (history: String) -> Unit,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxSize()
    ) {
        val query = remember { mutableStateOf(savedQuery) }

        Column(modifier = modifier.background(MaterialTheme.colorScheme.surface)) {
            SearchBar(
                modifier = modifier.background(MaterialTheme.colorScheme.surface),
                hint = hint,
                query = query,
                onQueryChange = onQueryChange,
                onCancelClick = onCancelClick,
                onSearchClick = onSearchClick,
            )

            SearchHistoryView(
                isVisible = histories.isNotEmpty(),
                histories = histories,
                onHistoryClick = {text->
                    query.value = text
                    onHistoryClick(text)
                },
                onClearHistoryClick = onClearHistoryClick,
            )

            content()
        }
    }
}

@Composable
fun SearchHistoryView(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    histories: List<String>,
    onHistoryClick: (history: String) -> Unit,
    onClearHistoryClick: (history: String) -> Unit,
) {
    val models = histories.map { history ->
        history.toHistoryCell(
            onClearClick = onClearHistoryClick,
            onClick = onHistoryClick
        )
    }

    val lazyListState = rememberLazyListState()
    if (lazyListState.isScrollInProgress) {
        HandleKeyboard(isKeyboardHidden = true)
    }
    BaseLazyColumn(
        modifier = modifier.background(MaterialTheme.colorScheme.surface),
        lazyListState = lazyListState,
        isVisible = isVisible,
        models = models
    )
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
private fun HandleKeyboard(isKeyboardHidden: Boolean) {
    val keyboardController = LocalSoftwareKeyboardController.current
    if (isKeyboardHidden) {
        keyboardController?.hide()
    }
}
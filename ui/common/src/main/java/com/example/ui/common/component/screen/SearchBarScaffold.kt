package com.example.ui.common.component.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.example.ui.common.component.bar.SearchBar

@Composable
fun SearchBarScaffold(
    modifier: Modifier = Modifier,
    hint: String,
    savedQuery: String,
    histories: List<String>,
    isHistoryVisible: Boolean,
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
                isVisible = isHistoryVisible,
                histories = histories,
                onHistoryClick = { text ->
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
    if (isVisible.not()) return
    val models = histories.map { history ->
        history.toHistoryCell(
            onClearClick = onClearHistoryClick,
            onClick = onHistoryClick
        )
    }

    val lazyListState = rememberLazyListState()
    if (lazyListState.isScrollInProgress) {
        HandleKeyboard()
    }
    LazyColumn(
        modifier = modifier,
        state = lazyListState,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(items = models) { it() }
    }
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
private fun HandleKeyboard(isKeyboardHidden: Boolean = true) {
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(key1 = isKeyboardHidden) {
        if (isKeyboardHidden) {
            keyboardController?.hide()
        }
    }
}
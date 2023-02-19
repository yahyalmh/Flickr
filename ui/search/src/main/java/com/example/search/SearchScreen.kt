package com.example.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.data.common.database.bookmark.PhotoEntity
import com.example.data.common.model.Photo
import com.example.flickr.ui.search.R
import com.example.ui.common.BaseScreen
import com.example.ui.common.component.*
import com.example.ui.common.component.cell.PhotoCell
import com.example.ui.common.component.cell.PhotoShimmerCell
import com.example.ui.common.component.icon.AppIcons
import com.example.ui.common.component.screen.SearchBarSurface
import com.example.ui.common.component.view.AutoRetryView
import com.example.ui.common.component.view.EmptyView
import com.example.ui.common.component.view.RetryView

@Composable
fun SearchScreen() = BaseScreen(hiltViewModel<SearchViewModel>()) { viewModel ->
    SearchViewContent(
        uiState = viewModel.state,
    ) { event: SearchUiEvent -> viewModel.onEvent(event) }
}

@Composable
private fun SearchViewContent(
    modifier: Modifier = Modifier,
    uiState: SearchUiState,
    onUiEvent: (SearchUiEvent) -> Unit,
) {
    HandleKeyboard(uiState.isKeyboardHidden)
    SearchBarSurface(modifier = modifier.background(MaterialTheme.colorScheme.surface),
        hint = stringResource(id = R.string.searchBarHint),
        savedQuery = uiState.query,
        histories = uiState.histories.map { it.text },
        isHistoryVisible = uiState.isHistoryVisible,
        onQueryChange = { query -> onUiEvent(SearchUiEvent.QueryChange(query)) },
        onCancelClick = { onUiEvent(SearchUiEvent.ClosSearch) },
        onSearchClick = { text -> onUiEvent(SearchUiEvent.OnSaveSearch(text)) },
        onHistoryClick = { history -> onUiEvent(SearchUiEvent.OnHistoryClick(history)) },
        onClearHistoryClick = { history -> onUiEvent(SearchUiEvent.OnClearHistoryClick(history)) }) {
        SearchShimmerView(
            modifier = modifier,
            isVisible = uiState.isLoading,
        )

        AutoRetryView(
            isVisible = uiState.isAutoRetry,
            errorMessage = uiState.autoRetryMessage,
            icon = AppIcons.Warning,
            hint = stringResource(id = R.string.searchAutoRetryHint)
        )

        RetryView(isVisible = uiState.isRetry,
            retryMessage = uiState.retryMessage,
            icon = AppIcons.Warning,
            onRetry = { onUiEvent(SearchUiEvent.Retry) })

        StartView(
            modifier = modifier,
            isVisible = uiState.isStart,
        )

        EmptyView(
            modifier = modifier,
            isVisible = uiState.isEmpty,
            icon = AppIcons.Search,
            message = buildNoResultHint(uiState.query)
        )

        DataView(
            modifier = modifier,
            isVisible = uiState.isLoaded || uiState.isPagination,
            isPagination = uiState.isPagination,
            result = uiState.result,
            bookmarkedPhotos = uiState.bookmarkedPhotos,
            onUiEvent = onUiEvent,
        )
    }
}

@Composable
private fun DataView(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    isPagination: Boolean,
    result: List<Photo>,
    bookmarkedPhotos: List<PhotoEntity>,
    onUiEvent: (SearchUiEvent) -> Unit,
) {
    if (isVisible) {
        val lazyListState = rememberLazyGridState()
        SetupListPagination(lazyListState = lazyListState,
            onPagination = { onUiEvent(SearchUiEvent.OnPagination) })
        if (lazyListState.isScrollInProgress) {
            HandleKeyboard(isKeyboardHidden = true)
        }

        LazyVerticalGrid(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
            state = lazyListState,
            columns = GridCells.Adaptive(minSize = 150.dp)
        ) {
            items(items = result.distinctBy { it.id }, key = { item -> item.id }) { photo ->
                val leadingIcon = if (bookmarkedPhotos.any { it.id == photo.id }) {
                    AppIcons.Favorite
                } else {
                    AppIcons.FavoriteBorder
                }
                PhotoCell(address = photo.getImageUrl(),
                    title = photo.title,
                    leadingIcon = leadingIcon,
                    onClick = { onUiEvent(SearchUiEvent.OnPhotoClick(photo.id)) },
                    onLeadingIconClick = { onUiEvent(SearchUiEvent.OnBookmark(photo)) })
            }
            // Add a shimmer cell while pagination
            if (isPagination) {
                item { PhotoShimmerCell() }
            }
        }
    }
}

@Composable
private fun SetupListPagination(
    lazyListState: LazyGridState,
    reservedItemCount: Int = 6,
    onPagination: () -> Unit
) {
    val shouldFetchNextPage = remember {
        derivedStateOf {
            val stackSize = lazyListState.layoutInfo.totalItemsCount - reservedItemCount
            val lastVisibleItem = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
            if (lastVisibleItem != null) {
                lastVisibleItem >= stackSize
            } else {
                false
            }
        }
    }
    LaunchedEffect(key1 = shouldFetchNextPage.value) {
        if (shouldFetchNextPage.value) {
            onPagination()
        }
    }
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
private fun HandleKeyboard(isKeyboardHidden: Boolean) {
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(key1 = isKeyboardHidden) {
        if (isKeyboardHidden) {
            keyboardController?.hide()
        }
    }
}


@Composable
fun SearchShimmerView(
    modifier: Modifier,
    isVisible: Boolean,
) {
    if (isVisible) {
        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Adaptive(minSize = 150.dp)
        ) {
            items(10) { PhotoShimmerCell() }
        }
    }
}

@Composable
private fun StartView(
    modifier: Modifier, isVisible: Boolean
) {
    EmptyView(
        modifier = modifier,
        isVisible = isVisible,
        icon = AppIcons.Search,
        message = stringResource(id = R.string.startSearchHint)
    )
}

@Composable
private fun buildNoResultHint(query: String) = buildAnnotatedString {
    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
        append(stringResource(id = R.string.noItemFound))
    }
    withStyle(
        style = SpanStyle(
            color = Color.Green,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
        )
    ) {
        append(" $query")
    }
}

@Preview
@Composable
fun SearchPreview() {
    SearchViewContent(
        uiState = SearchUiState.Start,
        onUiEvent = {},
    )
}
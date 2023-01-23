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
import com.example.ui.common.component.*
import com.example.ui.common.component.cell.PhotoCell
import com.example.ui.common.component.cell.PhotoShimmerCell
import com.example.ui.common.component.icon.AppIcons
import com.example.ui.common.component.screen.SearchBarScaffold
import com.example.ui.common.component.view.AutoRetryView
import com.example.ui.common.component.view.EmptyView
import com.example.ui.common.component.view.RetryView

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel()
) {
    SearchViewContent(modifier = modifier,
        uiState = viewModel.state.value,
        onRetry = { viewModel.onEvent(SearchUiEvent.Retry) },
        onQueryChange = { query -> viewModel.onEvent(SearchUiEvent.QueryChange(query)) },
        onSearchClick = { text -> viewModel.onEvent(SearchUiEvent.OnSaveSearch(text)) },
        onCancelClick = { viewModel.onEvent(SearchUiEvent.ClosSearch) },
        onBookmarkClick = { photo -> viewModel.onEvent(SearchUiEvent.OnBookmark(photo)) },
        onPhotoClick = { photoId -> viewModel.onEvent(SearchUiEvent.OnPhotoClick(photoId)) },
        onHistoryClick = { history ->
            viewModel.onEvent(SearchUiEvent.OnHistoryClick(history))
        },
        onClearHistoryClick = { history ->
            viewModel.onEvent(SearchUiEvent.OnClearHistoryClick(history))
        },
        onPagination = { viewModel.onEvent(SearchUiEvent.OnPagination) })
}

@Composable
private fun SearchViewContent(
    modifier: Modifier = Modifier,
    uiState: SearchUiState,
    onRetry: () -> Unit,
    onCancelClick: () -> Unit,
    onQueryChange: (query: String) -> Unit,
    onSearchClick: (text: String) -> Unit,
    onBookmarkClick: (photo: Photo) -> Unit,
    onPhotoClick: (photoId: String) -> Unit,
    onHistoryClick: (history: String) -> Unit,
    onClearHistoryClick: (history: String) -> Unit,
    onPagination: () -> Unit,
) {
    HandleKeyboard(uiState.isKeyboardHidden)
    SearchBarScaffold(
        modifier = modifier.background(MaterialTheme.colorScheme.surface),
        hint = stringResource(id = R.string.searchBarHint),
        savedQuery = uiState.query,
        histories = uiState.histories.map { it.text },
        onQueryChange = onQueryChange,
        onCancelClick = onCancelClick,
        onSearchClick = onSearchClick,
        onHistoryClick = onHistoryClick,
        onClearHistoryClick = onClearHistoryClick
    ) {
        SearchShimmerView(
            modifier = modifier,
            isVisible = uiState.isLoading,
        )

        AutoRetryView(
            isVisible = uiState.isAutoRetry,
            errorMessage = uiState.autoRetryMsg,
            icon = AppIcons.Warning,
            hint = stringResource(id = R.string.searchAutoRetryHint)
        )

        RetryView(
            isVisible = uiState.isRetry,
            retryMessage = uiState.retryMsg,
            icon = AppIcons.Warning,
            onRetry = onRetry
        )

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
            onPhotoClick = onPhotoClick,
            onBookmarkClick = onBookmarkClick,
            onPagination = onPagination,
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
    onPhotoClick: (photoId: String) -> Unit,
    onBookmarkClick: (photo: Photo) -> Unit,
    onPagination: () -> Unit,
) {
    if (isVisible) {
        val lazyListState = rememberLazyGridState()
        SetupListPagination(lazyListState = lazyListState, onPagination = onPagination)
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
            items(
                items = result.distinctBy { it.id },
                key = { item -> item.id }
            ) { photo ->
                val leadingIcon = if (bookmarkedPhotos.any { it.id == photo.id }) {
                    AppIcons.Favorite
                } else {
                    AppIcons.FavoriteBorder
                }
                PhotoCell(address = photo.getImageUrl(),
                    title = photo.title,
                    leadingIcon = leadingIcon,
                    onClick = { onPhotoClick(photo.id) },
                    onLeadingIconClick = { onBookmarkClick(photo) })
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
    if (isKeyboardHidden) {
        keyboardController?.hide()
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
        uiState = SearchUiState.Loading("", emptyList()),
        onRetry = {},
        onCancelClick = {},
        onQueryChange = {},
        onSearchClick = {},
        onBookmarkClick = {},
        onHistoryClick = {},
        onClearHistoryClick = {},
        onPagination = {},
        onPhotoClick = {},
    )
}
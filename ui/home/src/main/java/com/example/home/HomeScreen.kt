package com.example.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.data.common.database.bookmark.PhotoEntity
import com.example.data.common.ext.RandomString
import com.example.flickr.ui.home.R
import com.example.ui.common.BaseScreen
import com.example.ui.common.ReferenceDevices
import com.example.ui.common.component.cell.PhotoCell
import com.example.ui.common.component.cell.PhotoShimmerCell
import com.example.ui.common.component.icon.AppIcons
import com.example.ui.common.component.screen.TopBarScaffold
import com.example.ui.common.component.view.EmptyView
import com.example.ui.common.component.view.RetryView
import com.example.ui.common.ext.createComposable
import com.example.ui.common.test.TestTag.SHIMMER_VIEW

@Composable
fun HomeScreen() = BaseScreen(hiltViewModel<HomeViewModel>()) { viewModel ->
    HomeScreenContent(
        uiState = viewModel.state,
        onRetry = { viewModel.onEvent(HomeUiEvent.OnRetry) },
        onSearchClicked = { viewModel.onEvent(HomeUiEvent.OnSearchClick) },
        onPhotoClick = { photoId -> viewModel.onEvent(HomeUiEvent.OnPhotoClick(photoId)) },
        onBookmarkClick = { photoId -> viewModel.onEvent(HomeUiEvent.OnBookmarkClick(photoId)) },
    )
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    onRetry: () -> Unit = {},
    onSearchClicked: () -> Unit = {},
    onPhotoClick: (photoId: String) -> Unit = {},
    onBookmarkClick: (photoId: String) -> Unit = {},
) {
    TopBarScaffold(
        title = stringResource(id = R.string.bookmark),
        navigationIcon = AppIcons.Menu,
        navigationIconContentDescription = stringResource(id = R.string.menu),
        actionIcon = AppIcons.Search,
        actionIconContentDescription = stringResource(id = R.string.searchIconContentDescription),
        onActionClick = { onSearchClicked() }
    ) { padding ->
        HomeShimmerView(
            modifier = modifier.padding(padding),
            isVisible = uiState.isLoading
        )

        EmptyView(
            modifier = modifier,
            isVisible = uiState.isEmpty,
            icon = AppIcons.FavoriteBorder,
            message = stringResource(id = R.string.noBookmarkMessage)
        )

        RetryView(
            isVisible = uiState.isRetry,
            retryMessage = uiState.retryMessage,
            icon = AppIcons.Warning,
            onRetry = onRetry
        )

        DataView(
            modifier = modifier.padding(padding),
            isVisible = uiState.isLoaded,
            bookmarkedPhotos = uiState.bookmarkedPhotos,
            onPhotoClick = onPhotoClick,
            onBookmarkClick = onBookmarkClick
        )
    }
}

@Composable
private fun HomeShimmerView(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
) {
    if (isVisible) {
        LazyVerticalGrid(
            modifier = modifier.testTag(SHIMMER_VIEW),
            columns = GridCells.Adaptive(minSize = 150.dp)
        ) {
            items(10) { PhotoShimmerCell() }
        }
    }
}

@Composable
private fun DataView(
    modifier: Modifier,
    isVisible: Boolean,
    bookmarkedPhotos: List<PhotoEntity>,
    onPhotoClick: (photoId: String) -> Unit,
    onBookmarkClick: (photoId: String) -> Unit
) {
    if (isVisible) {
        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Adaptive(minSize = 150.dp)
        ) {
            items(
                items = bookmarkedPhotos.distinctBy { it.id },
                key = { item -> item.id }
            ) { photo ->
                PhotoCell(
                    address = photo.localAddress,
                    title = photo.title,
                    leadingIcon = AppIcons.Favorite,
                    onClick = { onPhotoClick(photo.id) },
                    onLeadingIconClick = { onBookmarkClick(photo.id) }
                )
            }
        }
    }
}

@Composable
@Preview
fun HomeShimmerPreview() {
    HomeShimmerView(isVisible = true)
}

@Composable
@ReferenceDevices
fun DataPreview() = DataView(
    modifier = Modifier,
    bookmarkedPhotos = createComposable(10) { photoEntityStub() },
    isVisible = true,
    onPhotoClick = {},
    onBookmarkClick = {}
)

internal fun photoEntityStub(): PhotoEntity = PhotoEntity(
    id = RandomString(),
    imageUrl = RandomString(),
    localAddress = RandomString(),
    title = RandomString(),
    timestamp = RandomString()
)
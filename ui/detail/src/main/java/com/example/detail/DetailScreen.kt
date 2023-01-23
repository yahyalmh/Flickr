package com.example.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.data.common.model.PhotoDetail
import com.example.flickr.ui.detail.R
import com.example.ui.common.component.icon.AppIcons
import com.example.ui.common.component.screen.TopBarScaffold
import com.example.ui.common.component.view.RetryView
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import com.example.flickr.ui.common.R.string as commonString

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = hiltViewModel()
) {
    DetailScreenContent(
        modifier = modifier,
        uiState = viewModel.state.value,
        onFavoriteClick = { photoDetail ->
            viewModel.onEvent(
                DetailUiEvent.OnBookmarkClick(
                    photoDetail
                )
            )
        },
        onBackClick = { viewModel.onEvent(DetailUiEvent.NavigationBack) },
        onRetry = { viewModel.onEvent(DetailUiEvent.Retry) }
    )
}

@Composable
private fun DetailScreenContent(
    modifier: Modifier,
    uiState: DetailUiState,
    onFavoriteClick: (photoDetail: PhotoDetail?) -> Unit,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
) {
    TopBarScaffold(
        title = stringResource(id = R.string.detail),
        actionIcon = if (uiState.isBookmarked) {
            AppIcons.Favorite
        } else {
            AppIcons.FavoriteBorder
        },
        actionIconContentDescription = stringResource(id = R.string.favoriteIcon),
        onActionClick = { onFavoriteClick(uiState.photoDetail) },
        actionIconColor = Color.Red,
        navigationIcon = AppIcons.ArrowBack,
        onNavigationClick = { onBackClick() }
    ) { padding ->

        DetailShimmerView(
            modifier = modifier.padding(padding),
            isVisible = uiState.isLoading
        )

        RetryView(
            modifier = modifier,
            isVisible = uiState.isRetry,
            retryMessage = uiState.retryMessage,
            icon = AppIcons.Warning,
            onRetry = onRetry
        )

        DataView(
            modifier = modifier.padding(padding),
            isVisible = uiState.isLoaded,
            photoDetail = uiState.photoDetail
        )
    }
}

@Composable
private fun DataView(
    modifier: Modifier,
    isVisible: Boolean,
    photoDetail: PhotoDetail?,
) {
    if (isVisible) {
        var isImageLoading by remember { mutableStateOf(true) }
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(modifier = Modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(15.dp))
                .fillMaxWidth()
                .placeholder(
                    visible = isImageLoading,
                    color = Color.LightGray,
                    highlight = PlaceholderHighlight.shimmer(Color.DarkGray),
                    shape = RoundedCornerShape(15.dp),
                )
                .aspectRatio(1f / 1f),
                model = photoDetail?.getImageUrl(),
                contentScale = ContentScale.FillBounds,
                contentDescription = stringResource(id = commonString.imageContentDescription),
                onSuccess = { isImageLoading = false })
            photoDetail?.let {
                TextWithDescription(title = "Title", description = it.title ?: "No title")
                TextWithDescription(title = "Description", description = it.description)
                TextWithDescription(title = "Views", description = it.views.toString())
                TextWithDescription(title = "Owner", description = it.owner.toString())
                TextWithDescription(title = "Usage", description = it.usage.toString())
                TextWithDescription(title = "Dates", description = it.dates.toString())
                TextWithDescription(title = "Tags", description = it.tags.joinToString("\n"))
            }
        }
    }
}

@Composable
private fun TextWithDescription(title: String, description: String) {
    Card(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.background), shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 14.dp, bottom = 5.dp),
            style = MaterialTheme.typography.titleSmall,
            text = title
        )

        Divider(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 8.dp, end = 12.dp, bottom = 8.dp),
            style = MaterialTheme.typography.bodyLarge,
            text = description
        )
    }
}

@Composable
fun DetailShimmerView(
    modifier: Modifier = Modifier,
    isVisible: Boolean
) {
    if (isVisible) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .aspectRatio(1f / 1f)
                    .placeholder(
                        visible = true,
                        color = Color.LightGray,
                        highlight = PlaceholderHighlight.shimmer(Color.DarkGray)
                    ),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .placeholder(
                        visible = true,
                        color = Color.LightGray,
                        highlight = PlaceholderHighlight.shimmer(Color.DarkGray)
                    ),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .placeholder(
                        visible = true,
                        color = Color.LightGray,
                        highlight = PlaceholderHighlight.shimmer(Color.DarkGray)
                    ),
            )
        }
    }
}

@Preview
@Composable
fun DetailShimmerPreview() {
    DetailShimmerView(isVisible = true)
}

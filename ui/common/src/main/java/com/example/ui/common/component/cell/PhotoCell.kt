package com.example.ui.common.component.cell

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.flickr.ui.common.R
import com.example.ui.common.component.icon.AppIcons
import com.example.ui.common.ext.RandomString
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoCell(
    modifier: Modifier = Modifier,
    address: String,
    title: String,
    leadingIcon: ImageVector? = null,
    onClick: () -> Unit = {},
    onLeadingIconClick: () -> Unit
) {
    var isImageLoading by remember { mutableStateOf(true) }
    Card(
        modifier = modifier
            .height(IntrinsicSize.Max)
            .fillMaxWidth()
            .padding(6.dp),
        onClick = onClick,
        shape = RoundedCornerShape(10.dp)
    ) {
        Box(modifier = modifier) {
            Column(modifier = modifier.fillMaxSize()) {
                AsyncImage(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .aspectRatio(16f / 9f)
                        .placeholder(
                            visible = isImageLoading,
                            color = Color.LightGray,
                            highlight = PlaceholderHighlight.shimmer(Color.DarkGray),
                            shape = RoundedCornerShape(10.dp),
                        )
                        .clip(RoundedCornerShape(8.dp)),
                    model = address,
                    contentDescription = stringResource(id = R.string.imageContentDescription),
                    contentScale = ContentScale.FillBounds,
                    onSuccess = { isImageLoading = false }
                )
                Text(
                    modifier = modifier
                        .padding(start = 4.dp, bottom = 4.dp)
                        .padding(4.dp),
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis,
                    text = title
                )
            }
            if (leadingIcon != null) {
                FilledTonalIconButton(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .size(28.dp),
                    onClick = { onLeadingIconClick() },
                ) {
                    Icon(
                        modifier = Modifier.padding(4.dp),
                        imageVector = leadingIcon,
                        contentDescription = "",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}

@Composable
fun PhotoShimmerCell(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .height(IntrinsicSize.Max)
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Box(modifier = modifier) {
            Column(modifier = modifier.fillMaxSize()) {
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .aspectRatio(16f / 9f)
                        .placeholder(
                            visible = true,
                            color = Color.LightGray,
                            highlight = PlaceholderHighlight.shimmer(Color.DarkGray),
                            shape = RoundedCornerShape(10.dp),
                        )
                        .clip(RoundedCornerShape(8.dp)),
                )
                Box(
                    modifier = modifier
                        .padding(start = 4.dp, bottom = 4.dp)
                        .padding(4.dp)
                        .fillMaxWidth()
                        .height(16.dp)
                        .clip(CircleShape)
                        .placeholder(
                            visible = true,
                            color = Color.LightGray,
                            highlight = PlaceholderHighlight.shimmer(Color.DarkGray)
                        )
                )
            }
            FilledTonalIconButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(28.dp),
                onClick = {},
            ) {
                Icon(
                    modifier = Modifier.padding(4.dp),
                    imageVector = AppIcons.FavoriteBorder,
                    contentDescription = "",
                    tint = Color.LightGray,
                )
            }
        }
    }
}

@Composable
@Preview
fun PhotoCellPreview() {
    PhotoCell(
        onClick = {},
        address = RandomString.next(),
        title = RandomString.next(),
        leadingIcon = AppIcons.Favorite
    ) {}
}

@Composable
@Preview
fun ShimmerPreview() {
    PhotoShimmerCell()
}
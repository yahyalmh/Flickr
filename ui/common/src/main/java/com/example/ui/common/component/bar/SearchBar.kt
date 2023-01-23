package com.example.ui.common.component.bar

import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.flickr.ui.common.R
import com.example.ui.common.component.icon.AppIcons

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String,
    query: MutableState<String>,
    onQueryChange: (query: String) -> Unit,
    onCancelClick: () -> Unit,
    onSearchClick: (text: String) -> Unit
) {
    val focusRequester = FocusRequester()
    val focusManager = LocalFocusManager.current

    Row(
        modifier = modifier
            .shadow(
                elevation = 5.dp,
                spotColor = MaterialTheme.colorScheme.onBackground
            )
            .background(MaterialTheme.colorScheme.background)
            .padding(3.dp)
            .zIndex(1F),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp)
                .background(
                    MaterialTheme.colorScheme.secondary,
                    RoundedCornerShape(20)
                )
                .padding(6.dp)
                .shadow(elevation = (-5).dp)
                .zIndex(-1f),
            verticalAlignment = Alignment.CenterVertically

        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = AppIcons.Search,
                contentDescription = stringResource(id = R.string.search),
                tint = MaterialTheme.colorScheme.onSecondary
            )
            BasicTextField(modifier = Modifier
                .focusRequester(focusRequester)
                .weight(1f)
                .padding(start = 2.dp)
                .height(24.dp)
                .background(color = Color.Transparent, shape = RoundedCornerShape(20)),
                singleLine = true,
                keyboardActions = KeyboardActions(onSearch = {
                    focusManager.clearFocus()
                    onSearchClick(query.value)
                }),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onSecondary),
                value = query.value,
                textStyle = MaterialTheme.typography.bodyLarge
                    .copy(color = MaterialTheme.colorScheme.onSecondary),
                onValueChange = {
                    query.value = it
                    onQueryChange(it)
                },
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,

                        ) {
                        AnimatedVisibility(
                            visible = query.value.isEmpty(),
                            enter = fadeIn(initialAlpha = 0.3f),
                            exit = fadeOut()
                        ) {
                            Text(
                                text = hint,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSecondary,
                            )
                        }
                    }
                    innerTextField()
                }
            )
            AnimatedVisibility(
                visible = query.value.isNotEmpty(),
                enter = fadeIn(initialAlpha = 0.3f),
                exit = fadeOut()
            ) {
                Icon(
                    modifier = Modifier
                        .clickable {
                            query.value = ""
                            onQueryChange(query.value)
                        }
                        .size(20.dp),
                    imageVector = AppIcons.Close,
                    contentDescription = stringResource(id = R.string.search),
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
        TextButton(
            modifier = Modifier
                .wrapContentSize()
                .padding(start = 4.dp, end = 4.dp),
            contentPadding = PaddingValues(4.dp),
            onClick = onCancelClick
        ) {
            Text(
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge,
                text = stringResource(id = R.string.cancel)
            )
        }
    }
    SideEffect { focusRequester.requestFocus() }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SearchPreviewDark() {
    SearchBar(
        hint = "Search",
        query = remember { mutableStateOf("Test") },
        onQueryChange = {},
        onSearchClick = {},
        onCancelClick = {},
    )
}

@Preview(showBackground = true, showSystemUi = false, uiMode = UI_MODE_NIGHT_MASK)
@Composable
fun SearchPreviewLight() {
    SearchBar(
        hint = "Search",
        query = remember { mutableStateOf("Test") },
        onQueryChange = {},
        onSearchClick = {},
        onCancelClick = {},
    )
}
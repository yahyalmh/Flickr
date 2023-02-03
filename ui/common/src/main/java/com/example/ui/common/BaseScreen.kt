package com.example.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner

@Composable
inline fun <T : UIState, E : UIEvent, VM : BaseViewModel<T, E>> BaseScreen(
    viewModel: VM,
    content: @Composable (viewModel: VM) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(viewModel)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(viewModel)
        }
    }
    content(viewModel)
}
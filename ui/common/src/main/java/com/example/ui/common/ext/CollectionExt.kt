package com.example.ui.common.ext

import androidx.compose.runtime.Composable

@Composable
fun <T> create(count: Int = 1, creator: @Composable (Int) -> T): List<T> {
    val result = mutableListOf<T>()
    for (index in 0 until count) {
        result.add(creator(index))
    }
    return result.toList()
}
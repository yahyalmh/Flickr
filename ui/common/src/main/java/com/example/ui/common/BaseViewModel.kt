package com.example.ui.common

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<T : UIState, E : UIEvent>(initialState: T) : ViewModel() {
    private val internalSate: MutableState<T> = mutableStateOf(initialState)

    val state: T
    get() = internalSate.value

    abstract fun onEvent(event: E)

    protected fun setState(state: T) {
        internalSate.value = state
    }

}

interface UIState

interface UIEvent

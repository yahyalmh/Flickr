package com.example.ui.common

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

/**
 * @author yaya (@yahyalmh)
 * @since 29th September 2022
 */

abstract class BaseViewModel<T : UIState, E : UIEvent>(initialState: T) : ViewModel() {
    private var internalSate: MutableState<T> = mutableStateOf(initialState)
    val state: State<T> = internalSate

    abstract fun onEvent(event: E)

    protected fun setState(state: T) {
        internalSate.value = state
    }

}

interface UIState

interface UIEvent

package com.example.ui.common

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.example.ui.common.utility.JobDisposable

abstract class BaseViewModel<T : UIState, E : UIEvent>(
    initialState: T
) : ViewModel(), DefaultLifecycleObserver {
    private val internalSate: MutableState<T> = mutableStateOf(initialState)
    protected val jobDisposable by lazy { JobDisposable() }
    val state: T get() = internalSate.value

    abstract fun onEvent(event: E)

    protected fun setState(state: T) {
        internalSate.value = state
    }

    override fun onStop(owner: LifecycleOwner) {
        jobDisposable.dispose()
    }
}

interface UIState

interface UIEvent

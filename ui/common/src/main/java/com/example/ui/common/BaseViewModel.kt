package com.example.ui.common

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.example.ui.common.utility.JobDisposable

abstract class BaseViewModel<S : UIState, E : UIEvent>(
    initialState: S
) : ViewModel(), DefaultLifecycleObserver {
    private val internalSate: MutableState<S> = mutableStateOf(initialState)
    protected val jobDisposable by lazy { JobDisposable() }
    val state: S get() = internalSate.value

    abstract fun onEvent(event: E)

    protected fun setState(state: S) {
        internalSate.value = state
    }

    override fun onStop(owner: LifecycleOwner) {
        jobDisposable.dispose()
    }
}

interface UIState

interface UIEvent

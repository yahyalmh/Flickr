package com.example.main

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.main.MainUiState.ConnectivityStatus
import com.example.main.MainUiState.Start
import com.example.ui.common.BaseViewModel
import com.example.ui.common.UIEvent
import com.example.ui.common.UIState
import com.example.ui.common.connectivity.ConnectivityMonitor
import com.example.ui.common.ext.addTo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val connectivityMonitor: ConnectivityMonitor,
    val navController: NavHostController,
) : BaseViewModel<MainUiState, MainUiEvent>(Start) {
    private var isAppLaunchedForFirstTime: Boolean = true

    override fun onStart(owner: LifecycleOwner) {
        isAppLaunchedForFirstTime = true
        observeConnectivityState()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeConnectivityState() {
        connectivityMonitor.isOnline
            .distinctUntilChanged()
            .mapLatest { isOnline ->
                val shouldShowNetStatus = checkConnectivityStatusVisibility(isOnline)
                setState(
                    ConnectivityStatus(
                        state.copy(
                            isOnline = isOnline,
                            isStatusVisible = shouldShowNetStatus
                        )
                    )
                )
            }
            .launchIn(viewModelScope)
            .addTo(jobDisposable)
    }

    private fun checkConnectivityStatusVisibility(isOnline: Boolean): Boolean {
        val shouldShowNetStatus = (isAppLaunchedForFirstTime && isOnline).not()
        isAppLaunchedForFirstTime = false
        return shouldShowNetStatus
    }

    override fun onEvent(event: MainUiEvent) {}
}

sealed interface MainUiEvent : UIEvent

open class MainUiState(
    val isOnline: Boolean = false,
    val isConnectivityStatusVisible: Boolean = false,
) : UIState {
    constructor(state: MainUiState) : this(
        state.isOnline,
        state.isConnectivityStatusVisible
    )

    fun copy(
        isOnline: Boolean = this.isOnline,
        isStatusVisible: Boolean = this.isConnectivityStatusVisible,
    ) = MainUiState(
        isOnline = isOnline,
        isConnectivityStatusVisible = isStatusVisible,
    )

    object Start : MainUiState()
    class ConnectivityStatus(state: MainUiState) : MainUiState(state)
}


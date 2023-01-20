package com.example.main

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.main.nav.MainRout
import com.example.ui.common.BaseViewModel
import com.example.ui.common.UIEvent
import com.example.ui.common.UIState
import com.example.ui.common.connectivity.ConnectivityMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val connectivityMonitor: ConnectivityMonitor,
    val navController: NavHostController,
) : BaseViewModel<MainUiState, MainUiEvent>(MainUiState.HideNetStatusView) {
    private var isAppLaunchedForFirstTime: Boolean = true

    init {
        observeConnectivityState()
    }

    private fun observeConnectivityState() {
        connectivityMonitor.isOnline
            .distinctUntilChanged()
            .onEach { isOnline ->
                if (isOnline) {
                    handelOnlineState()
                } else {
                    setState(MainUiState.Offline)
                }
            }.launchIn(viewModelScope)
    }

    private fun handelOnlineState() {
        if (isAppLaunchedForFirstTime) {
            isAppLaunchedForFirstTime = false
            return
        }
        setState(MainUiState.Online)
        hideOnlineViewAfterWhile()
    }

    private fun hideOnlineViewAfterWhile() {
        val hideOnlineViewDelay: Long = 2000
        viewModelScope.launch {
            delay(hideOnlineViewDelay)
            setState(MainUiState.HideNetStatusView)
        }
    }

    override fun onEvent(event: MainUiEvent) {}
}

sealed interface MainUiEvent : UIEvent

sealed class MainUiState(
    val isOnlineViewVisible: Boolean = false,
    val isOfflineViewVisible: Boolean = false,
) : UIState {
    object HideNetStatusView : MainUiState(
        isOnlineViewVisible = false,
        isOfflineViewVisible = false,
    )

    object Offline : MainUiState(isOfflineViewVisible = true)
    object Online : MainUiState(isOnlineViewVisible = true)
}


package com.example.main

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.main.nav.AppNavHost
import com.example.main.theme.AppTheme
import com.example.ui.common.component.bar.ConnectivityStatusView

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState = viewModel.state.value
    AppTheme {
        ContentView(
            modifier = modifier,
            uiState = uiState,
            navController = viewModel.navController,
        )
    }
}

@Composable
private fun ContentView(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    uiState: MainUiState,
) {
    Column {
        ConnectivityStatusView(
            modifier = modifier,
            isOnlineViewVisible = uiState.isOnlineViewVisible,
            isOfflineViewVisible = uiState.isOfflineViewVisible
        )
        Scaffold(
            modifier = modifier.fillMaxSize(),
            contentColor = MaterialTheme.colorScheme.surface,
        ) { paddingValues ->
            SetupAppNavHost(navController, paddingValues)
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun SetupAppNavHost(
    navHostController: NavHostController,
    padding: PaddingValues = PaddingValues.Absolute()
) {
    AppNavHost(
        navController = navHostController,
        modifier = Modifier
            .padding(padding)
            .consumedWindowInsets(padding)
    )
}

@Composable
@Preview(
    showSystemUi = false,
    name = "OfflinePreview",
    device = Devices.PHONE
)
fun OfflineContentPreview() {
    val navController = rememberNavController()
    ContentView(
        uiState = MainUiState.Offline,
        navController = navController,
    )
}

@Composable
@Preview(
    showSystemUi = false,
    name = "OnlinePreview",
    device = Devices.PHONE,
    uiMode = UI_MODE_NIGHT_YES
)
fun OnlineContentPreview() {
    val navController = rememberNavController()
    ContentView(
        uiState = MainUiState.Online,
        navController = navController,
    )
}


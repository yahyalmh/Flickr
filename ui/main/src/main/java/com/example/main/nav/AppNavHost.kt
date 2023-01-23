package com.example.main.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.detail.nav.DetailRoute.Companion.detailGraph
import com.example.home.nav.HomeRoute.Companion.HOME_ROUTE
import com.example.home.nav.HomeRoute.Companion.homeGraph
import com.example.search.nav.SearchRoute.Companion.searchGraph

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = HOME_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        homeGraph()
        searchGraph()
        detailGraph()
    }
}
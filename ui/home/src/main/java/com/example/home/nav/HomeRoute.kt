package com.example.home.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.detail.nav.DetailRoute
import com.example.home.HomeScreen
import com.example.search.nav.SearchRoute.Companion.SEARCH_ROUTE
import javax.inject.Inject

class HomeRoute @Inject constructor(private val navController: NavHostController) {

    fun navigateToSearch(navOptions: NavOptions? = null) {
        navController.navigate(SEARCH_ROUTE, navOptions)
    }

    fun navigateToDetail(photoId: String, navOptions: NavOptions? = null) {
        navController.navigate("${DetailRoute.DETAIL_ROUTE}/$photoId", navOptions)
    }

    companion object {
        const val HOME_ROUTE = "home_route"

        fun NavGraphBuilder.homeGraph() {
            composable(route = HOME_ROUTE) { HomeScreen() }
        }
    }
}

package com.example.search.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.detail.nav.DetailRoute.Companion.DETAIL_ROUTE
import com.example.search.SearchScreen
import javax.inject.Inject

class SearchRoute @Inject constructor(private val navController: NavHostController) {

    fun popBackStack() = navController.popBackStack()

    fun navigateToDetail(photoId: String, navOptions: NavOptions? = null) {
        navController.navigate("$DETAIL_ROUTE/$photoId", navOptions)
    }

    companion object {
        const val SEARCH_ROUTE = "search_route"

        fun NavGraphBuilder.searchGraph() {
            composable(route = SEARCH_ROUTE) { SearchScreen() }
        }
    }
}
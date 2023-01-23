package com.example.detail.nav

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.detail.DetailScreen
import javax.inject.Inject

class DetailRoute @Inject constructor(private val navController: NavHostController) {
    fun popBackStack() = navController.popBackStack()

    companion object {
        const val DETAIL_ROUTE = "detail_route"

        @VisibleForTesting
        internal const val PHOTO_ID_ARG_KEY = "photoId"

        fun NavGraphBuilder.detailGraph() {
            composable(
                route = "$DETAIL_ROUTE/{$PHOTO_ID_ARG_KEY}",
                arguments = listOf(navArgument(PHOTO_ID_ARG_KEY) { type = NavType.StringType })
            ) {
                DetailScreen()
            }
        }
    }

    internal class DetailArgs(savedStateHandle: SavedStateHandle) {
        var photoId: String = savedStateHandle.get<String>(PHOTO_ID_ARG_KEY).toString()
    }
}

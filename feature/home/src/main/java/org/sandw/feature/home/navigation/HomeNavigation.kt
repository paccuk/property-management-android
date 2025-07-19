package org.sandw.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import org.sandw.feature.home.HomeRoute

const val HOME_ROUTE = "home_route"

fun NavController.navigateToHome(navOptions: NavOptions) = navigate(HOME_ROUTE, navOptions)

fun NavGraphBuilder.homeScreen(
    onPropertyClick: (String) -> Unit,
    onStatisticsClick: () -> Unit
) {
    composable(route = HOME_ROUTE) {
        HomeRoute(onPropertyClick, onStatisticsClick)
    }
}
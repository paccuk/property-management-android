package org.stkachenko.propertymanagement.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import org.stkachenko.propertymanagement.feature.home.HomeRoute

const val HOME_ROUTE = "home_route"

fun NavController.navigateToHome(navOptions: NavOptions) = navigate(HOME_ROUTE, navOptions)

fun NavGraphBuilder.homeScreen(
    onPropertyClick: () -> Unit,
) {
    composable(route = HOME_ROUTE) {
        HomeRoute(onPropertyClick)
    }
}
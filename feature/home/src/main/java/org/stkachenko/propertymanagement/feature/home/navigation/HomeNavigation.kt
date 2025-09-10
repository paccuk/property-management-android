package org.stkachenko.propertymanagement.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import org.stkachenko.propertymanagement.core.navigation_contract.HomeDestination.HOME_ROUTE
import org.stkachenko.propertymanagement.feature.home.HomeRoute


fun NavController.navigateToHome(navOptions: NavOptions) = navigate(HOME_ROUTE, navOptions)

fun NavGraphBuilder.homeScreen(
    onPropertyClick: () -> Unit,
    navController: NavController,
) {
    composable(route = HOME_ROUTE) {
        HomeRoute(
            onPropertyClick = onPropertyClick,
            navController = navController
        )
    }
}

// TODO("Виділити navigation-contract модуль (рекомендовано)")
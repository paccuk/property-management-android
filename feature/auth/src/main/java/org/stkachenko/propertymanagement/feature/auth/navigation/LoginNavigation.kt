package org.stkachenko.propertymanagement.feature.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import org.stkachenko.propertymanagement.core.navigation_contract.AuthDestination.LOGIN_ROUTE
import org.stkachenko.propertymanagement.feature.auth.LoginRoute

fun NavController.navigateToLogin(navOptions: NavOptions? = null) =
    navigate(LOGIN_ROUTE, navOptions)

fun NavGraphBuilder.loginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegistration: () -> Unit,
) {
    composable(
        route = LOGIN_ROUTE
    ) {
        LoginRoute(onLoginSuccess, onNavigateToRegistration)
    }
}
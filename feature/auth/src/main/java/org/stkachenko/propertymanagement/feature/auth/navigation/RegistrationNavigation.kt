package org.stkachenko.propertymanagement.feature.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import org.stkachenko.propertymanagement.core.navigation_contract.AuthDestination.REGISTRATION_ROUTE
import org.stkachenko.propertymanagement.feature.auth.RegistrationRoute

fun NavController.navigateToRegistration(navOptions: NavOptions? = null) =
    navigate(REGISTRATION_ROUTE, navOptions)

fun NavGraphBuilder.registrationScreen(
    onNavigateToCompleteRegistration: () -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    composable(
        route = REGISTRATION_ROUTE
    ) {
        RegistrationRoute(
            onNavigateToCompleteRegistration = onNavigateToCompleteRegistration,
            onNavigateToLogin = onNavigateToLogin,
        )
    }
}
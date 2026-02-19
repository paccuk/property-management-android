package org.stkachenko.propertymanagement.feature.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import org.stkachenko.propertymanagement.core.navigation_contract.AuthDestination.COMPLETE_REGISTRATION_ROUTE
import org.stkachenko.propertymanagement.feature.auth.CompleteRegistrationRoute

fun NavController.navigateToCompleteRegistration(navOptions: NavOptions? = null) =
    navigate(COMPLETE_REGISTRATION_ROUTE, navOptions)

fun NavGraphBuilder.completeRegistrationScreen(
    onCompleteRegistrationSuccess: () -> Unit,
) {
    composable(
        route = COMPLETE_REGISTRATION_ROUTE
    ) {
        CompleteRegistrationRoute(
            onCompleteRegistrationSuccess = onCompleteRegistrationSuccess,
        )
    }
}
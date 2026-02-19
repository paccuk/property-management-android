package org.stkachenko.propertymanagement.feature.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import org.stkachenko.propertymanagement.core.navigation_contract.ProfileDestination
import org.stkachenko.propertymanagement.feature.profile.ProfileRoute

fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    navigate(ProfileDestination.PROFILE_ROUTE, navOptions)
}

fun NavGraphBuilder.profileScreen(
    onNavigateToLogin: () -> Unit,
) {
    composable(route = ProfileDestination.PROFILE_ROUTE) {
        ProfileRoute(onNavigateToLogin = onNavigateToLogin)
    }
}

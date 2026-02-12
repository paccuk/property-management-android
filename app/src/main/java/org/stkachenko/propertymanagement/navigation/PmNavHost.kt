package org.stkachenko.propertymanagement.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import org.stkachenko.propertymanagement.feature.auth.navigation.completeRegistrationScreen
import org.stkachenko.propertymanagement.feature.auth.navigation.loginScreen
import org.stkachenko.propertymanagement.feature.auth.navigation.navigateToCompleteRegistration
import org.stkachenko.propertymanagement.feature.auth.navigation.navigateToLogin
import org.stkachenko.propertymanagement.feature.auth.navigation.navigateToRegistration
import org.stkachenko.propertymanagement.feature.auth.navigation.registrationScreen
import org.stkachenko.propertymanagement.feature.profile.navigation.navigateToProfile
import org.stkachenko.propertymanagement.feature.profile.navigation.profileScreen
import org.stkachenko.propertymanagement.feature.properties.navigation.navigateToProperties
import org.stkachenko.propertymanagement.feature.properties.navigation.navigateToPropertyDetails
import org.stkachenko.propertymanagement.feature.properties.navigation.propertiesScreen
import org.stkachenko.propertymanagement.feature.properties.navigation.propertyDetailsScreen
import org.stkachenko.propertymanagement.ui.PmAppState

@Composable
fun PmNavHost(
    appState: PmAppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    startDestination: String = TODO(),
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        loginScreen(
            onLoginSuccess = navController::navigateToProperties,
            onNavigateToRegistration = navController::navigateToRegistration,
        )
        registrationScreen(
            onNavigateToCompleteRegistration = navController::navigateToCompleteRegistration,
            onNavigateToLogin = navController::navigateToLogin,
        )
        completeRegistrationScreen(
            onCompleteRegistrationSuccess = navController::navigateToProperties,
        )

        propertiesScreen(onPropertyClick = navController::navigateToPropertyDetails)
        propertyDetailsScreen(onPropertyIdNotFound = navController::popBackStack)

        profileScreen(onNavigateToLogin = { appState.navigateToLogin() })
    }
}
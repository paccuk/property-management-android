package org.stkachenko.propertymanagement.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import org.stkachenko.propertymanagement.core.navigation_contract.HomeDestination.HOME_ROUTE
import org.stkachenko.propertymanagement.feature.home.navigation.homeScreen
import org.stkachenko.propertymanagement.ui.PmAppState

@Composable
fun PmNavHost(
    appState: PmAppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    startDestination: String = HOME_ROUTE
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        homeScreen(
            onPropertyClick = navController::navigateToPropertyDetails,
            navController = navController
        )
        propertiesScreen(onPropertyClick = navController::navigateToPropertyDetails)
        chatsScreen(onChatClick = navController::navigateToChatDetails)
        profileScreen(onEditProfileClick = navController::navigateToEditProfile)
    }
}
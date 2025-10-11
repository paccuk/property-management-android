package org.stkachenko.propertymanagement.feature.properties.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import org.stkachenko.propertymanagement.core.navigation_contract.PropertiesDestinations.PROPERTIES_ROUTE
import org.stkachenko.propertymanagement.feature.properties.PropertiesRoute


fun NavController.navigateToProperties(navOptions: NavOptions? = null) =
    navigate(PROPERTIES_ROUTE, navOptions)

fun NavGraphBuilder.propertiesScreen(onPropertyClick: (String) -> Unit) {
    composable(
        route = PROPERTIES_ROUTE
    ) {
        PropertiesRoute(onPropertyClick)
    }
}

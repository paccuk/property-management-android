package org.stkachenko.propertymanagement.feature.properties.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.stkachenko.propertymanagement.core.navigation_contract.PropertiesDestinations.PROPERTIES_ROUTE
import org.stkachenko.propertymanagement.core.navigation_contract.PropertiesDestinations.PROPERTY_DETAILS_ROUTE
import org.stkachenko.propertymanagement.core.navigation_contract.PropertiesDestinations.PROPERTY_DETAILS_ROUTE_BASE
import org.stkachenko.propertymanagement.core.navigation_contract.PropertiesDestinations.PROPERTY_ID_ARG
import org.stkachenko.propertymanagement.feature.properties.PropertyDetailsRoute

fun NavController.navigateToPropertyDetails(
    propertyId: String? = null,
    navOptions: NavOptions? = null
) {
    val route = if (propertyId != null) {
        "${PROPERTY_DETAILS_ROUTE_BASE}?${PROPERTY_ID_ARG}=$propertyId"
    } else {
        PROPERTIES_ROUTE
    }
    navigate(route, navOptions)
}

fun NavGraphBuilder.propertyDetailsScreen(onPropertyIdNotFound: () -> Unit) {
    composable(
        route = PROPERTY_DETAILS_ROUTE,
        arguments = listOf(navArgument(PROPERTY_ID_ARG) { type = NavType.StringType })
    ) { backStackEntry ->
        val propertyId = backStackEntry.arguments?.getString(PROPERTY_ID_ARG)
        if (propertyId == null) {
            onPropertyIdNotFound
            return@composable
        }
        PropertyDetailsRoute(propertyId)
    }
}

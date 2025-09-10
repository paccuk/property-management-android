package org.stkachenko.propertymanagement.feature.home.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.Flow
import org.stkachenko.propertymanagement.core.navigation_contract.ChatDestination.CHAT_ROUTE
import org.stkachenko.propertymanagement.core.navigation_contract.ProfileDestination.PROFILE_ROUTE
import org.stkachenko.propertymanagement.core.navigation_contract.PropertiesDestination.PROPERTIES_ROUTE
import org.stkachenko.propertymanagement.core.navigation_contract.StatisticsDestination.STATISTICS_ROUTE
import org.stkachenko.propertymanagement.feature.home.R

sealed class HomeCategory(
    @StringRes val titleResId: Int,
    val destinationRoute: String,
    val content: @Composable () -> Unit,
) {
    class Properties :
        HomeCategory(
            titleResId = R.string.feature_home_properties_category,
            destinationRoute = PROPERTIES_ROUTE,
            content = {},
        )

    class Statistics :
        HomeCategory(
            titleResId = R.string.feature_home_statistics_category,
            destinationRoute = STATISTICS_ROUTE,
            content = {},
        )

    companion object {
        fun getAllCategories(): List<HomeCategory> = listOf(
            Properties(),
            Statistics()
        )
    }
}

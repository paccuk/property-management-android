package org.sandw.pma.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.sandw.core.data.local.repository.UserPropertiesRepository
import org.sandw.core.data.util.NetworkMonitor
import org.sandw.pma.navigation.TopLevelDestination
import org.sandw.pma.navigation.TopLevelDestination.*

@Composable
fun rememberPmaAppState(
    networkMonitor: NetworkMonitor,
    userPropertiesRepository: UserPropertiesRepository,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): PmaAppState {
    return remember(
        navController,
        coroutineScope,
        networkMonitor,
        userPropertiesRepository
    ) {
        PmaAppState(
            navController = navController,
            coroutineScope = coroutineScope,
            networkMonitor = networkMonitor,
            userPropertiesRepository = userPropertiesRepository
        )
    }
}

@Stable
class PmaAppState(
    val navController: NavHostController,
    coroutineScope: CoroutineScope,
    networkMonitor: NetworkMonitor,
    userPropertiesRepository: UserPropertiesRepository,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            HOME_ROUTE -> HOME
            PROPERTIES_ROUTE -> PROPERTIES
            CHATS_ROUTE -> CHATS
            STATISTICS_ROUTE -> STATISTICS
            PROFILE_ROUTE -> PROFILE
            else -> null
        }

    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries


}
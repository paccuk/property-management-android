package org.stkachenko.propertymanagement.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import org.stkachenko.propertymanagement.core.designsystem.icon.PmIcons
import org.stkachenko.propertymanagement.core.designsystem.R


enum class TopLevelDestination(
    val selectedIcon: @Composable () -> Painter,
    val unselectedIcon: @Composable () -> Painter,
    val iconTextId: Int,
    val titleTextId: Int,
) {
    HOME(
        selectedIcon = { PmIcons.homeSelected() },
        unselectedIcon = { PmIcons.homeUnselected() },
        iconTextId = R.string.home,
        titleTextId = R.string.home,
    ),
    PROPERTIES(
        selectedIcon = { PmIcons.propertiesSelected() },
        unselectedIcon = { PmIcons.propertiesUnselected() },
        iconTextId = R.string.properties,
        titleTextId = R.string.properties,
    ),
    CHATS(
        selectedIcon = { PmIcons.chatsSelected() },
        unselectedIcon = { PmIcons.chatsUnselected() },
        iconTextId = R.string.chats,
        titleTextId = R.string.chats,
    ),
    STATISTICS(
        selectedIcon = { PmIcons.statisticsSelected() },
        unselectedIcon = { PmIcons.statisticsUnselected() },
        iconTextId = R.string.statistics,
        titleTextId = R.string.statistics,
    ),
    PROFILE(
        selectedIcon = { PmIcons.profileSelected() },
        unselectedIcon = { PmIcons.profileUnselected() },
        iconTextId = R.string.profile,
        titleTextId = R.string.profile,
    );
}

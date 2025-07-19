package org.sandw.pma.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import org.sandw.core.designsystem.icon.PmaIcons
import org.sandw.core.designsystem.R


enum class TopLevelDestination(
    val selectedIcon: @Composable () -> Painter,
    val unselectedIcon: @Composable () -> Painter,
    val iconTextId: Int,
    val titleTextId: Int,
) {
    HOME(
        selectedIcon = { PmaIcons.homeSelected() },
        unselectedIcon = { PmaIcons.homeUnselected() },
        iconTextId = R.string.home,
        titleTextId = R.string.home,
    ),
    PROPERTIES(
        selectedIcon = { PmaIcons.propertiesSelected() },
        unselectedIcon = { PmaIcons.propertiesUnselected() },
        iconTextId = R.string.properties,
        titleTextId = R.string.properties,
    ),
    CHATS(
        selectedIcon = { PmaIcons.chatsSelected() },
        unselectedIcon = { PmaIcons.chatsUnselected() },
        iconTextId = R.string.chats,
        titleTextId = R.string.chats,
    ),
    STATISTICS(
        selectedIcon = { PmaIcons.statisticsSelected() },
        unselectedIcon = { PmaIcons.statisticsUnselected() },
        iconTextId = R.string.statistics,
        titleTextId = R.string.statistics,
    ),
    PROFILE(
        selectedIcon = { PmaIcons.profileSelected() },
        unselectedIcon = { PmaIcons.profileUnselected() },
        iconTextId = R.string.profile,
        titleTextId = R.string.profile,
    );
}

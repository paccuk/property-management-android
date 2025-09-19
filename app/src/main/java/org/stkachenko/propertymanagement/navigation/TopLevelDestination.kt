package org.stkachenko.propertymanagement.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import org.stkachenko.propertymanagement.core.designsystem.icon.PmIcons
import org.stkachenko.propertymanagement.feature.properties.R as propertiesR
import org.stkachenko.propertymanagement.feature.statistics.R as statisticsR
import org.stkachenko.propertymanagement.feature.profile.R as profileR
import org.stkachenko.propertymanagement.feature.chats.R as chatsR


enum class TopLevelDestination(
    val selectedIcon: @Composable () -> ImageVector,
    val unselectedIcon: @Composable () -> ImageVector,
    val iconTextId: Int,
    val titleTextId: Int,
) {
    PROPERTIES(
        selectedIcon = { PmIcons.PropertiesSelected },
        unselectedIcon = { PmIcons.PropertiesUnselected },
        iconTextId = propertiesR.string.feature_properties_title,
        titleTextId = propertiesR.string.feature_properties_title,
    ),
    CHATS(
        selectedIcon = { PmIcons.ChatsSelected },
        unselectedIcon = { PmIcons.ChatsUnselected },
        iconTextId = chatsR.string.feature_chats_title,
        titleTextId = chatsR.string.feature_chats_title,
    ),
    STATISTICS(
        selectedIcon = { PmIcons.StatisticsSelected },
        unselectedIcon = { PmIcons.StatisticsUnselected },
        iconTextId = statisticsR.string.feature_statistics_title,
        titleTextId = statisticsR.string.feature_statistics_title,
    ),
    PROFILE(
        selectedIcon = { PmIcons.ProfileSelected },
        unselectedIcon = { PmIcons.ProfileUnselected },
        iconTextId = profileR.string.feature_profile_title,
        titleTextId = profileR.string.feature_profile_title,
    );
}

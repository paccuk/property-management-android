package org.stkachenko.propertymanagement.core.designsystem.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItemColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.stkachenko.propertymanagement.core.designsystem.icon.PmIcons
import org.stkachenko.propertymanagement.core.designsystem.theme.PmTheme

@Composable
fun RowScope.PmNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    alwaysShowLabel: Boolean = true,
    icon: @Composable () -> Unit,
    selectedIcon: @Composable () -> Unit = icon,
    label: @Composable (() -> Unit)? = null,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = if (selected) selectedIcon else icon,
        modifier = modifier,
        enabled = enabled,
        label = label,
        alwaysShowLabel = alwaysShowLabel,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = PmNavigationDefaults.navigationSelectedIconColor(),
            unselectedIconColor = PmNavigationDefaults.navigationContentColor(),
            selectedTextColor = PmNavigationDefaults.navigationSelectedIconColor(),
            unselectedTextColor = PmNavigationDefaults.navigationContentColor(),
            indicatorColor = PmNavigationDefaults.navigationIndicatorColor(),
        ),
    )
}

@Composable
fun PmNavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    NavigationBar(
        modifier = modifier,
        contentColor = PmNavigationDefaults.navigationContentColor(),
        tonalElevation = 0.dp,
        content = content
    )
}

@Composable
fun PmNavigationSuiteScaffold(
    navigationSuiteItems: PmNavigationSuiteScope.() -> Unit,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
    content: @Composable () -> Unit,
) {
    val layoutType = NavigationSuiteScaffoldDefaults
        .calculateFromAdaptiveInfo(windowAdaptiveInfo)
    val navigationSuiteItemColors = NavigationSuiteItemColors(
        navigationBarItemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = PmNavigationDefaults.navigationSelectedIconColor(),
            unselectedIconColor = PmNavigationDefaults.navigationContentColor(),
            selectedTextColor = PmNavigationDefaults.navigationSelectedIconColor(),
            unselectedTextColor = PmNavigationDefaults.navigationContentColor(),
            indicatorColor = PmNavigationDefaults.navigationIndicatorColor(),
        ),
        navigationRailItemColors = NavigationRailItemDefaults.colors(
            selectedIconColor = PmNavigationDefaults.navigationSelectedIconColor(),
            unselectedIconColor = PmNavigationDefaults.navigationContentColor(),
            selectedTextColor = PmNavigationDefaults.navigationSelectedIconColor(),
            unselectedTextColor = PmNavigationDefaults.navigationContentColor(),
            indicatorColor = PmNavigationDefaults.navigationIndicatorColor(),
        ),
        navigationDrawerItemColors = NavigationDrawerItemDefaults.colors(
            selectedIconColor = PmNavigationDefaults.navigationSelectedIconColor(),
            unselectedIconColor = PmNavigationDefaults.navigationContentColor(),
            selectedTextColor = PmNavigationDefaults.navigationSelectedIconColor(),
            unselectedTextColor = PmNavigationDefaults.navigationContentColor(),
        ),
    )

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            PmNavigationSuiteScope(
                navigationSuiteScope = this,
                navigationSuiteItemColors = navigationSuiteItemColors,
            ).run(navigationSuiteItems)
        },
        layoutType = layoutType,
        containerColor = Color.Transparent,
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            navigationBarContentColor = PmNavigationDefaults.navigationContentColor(),
            navigationRailContentColor = Color.Transparent,
        ),
        modifier = modifier
    ) {
        content()
    }
}

class PmNavigationSuiteScope internal constructor(
    private val navigationSuiteScope: NavigationSuiteScope,
    private val navigationSuiteItemColors: NavigationSuiteItemColors,
) {
    fun item(
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        icon: @Composable () -> Unit,
        selectedIcon: @Composable () -> Unit = icon,
        label: @Composable (() -> Unit)? = null,
    ) = navigationSuiteScope.item(
        selected = selected,
        onClick = onClick,
        icon = {
            if (selected) {
                selectedIcon()
            } else {
                icon()
            }
        },
        label = label,
        colors = navigationSuiteItemColors,
        modifier = modifier,
    )
}

object PmNavigationDefaults {
    @Composable
    fun navigationContentColor() = MaterialTheme.colorScheme.onSurfaceVariant

    @Composable
    fun navigationSelectedIconColor() = MaterialTheme.colorScheme.onPrimaryContainer

    @Composable
    fun navigationIndicatorColor() = MaterialTheme.colorScheme.primaryContainer
}

@ThemePreviews
@Composable
fun PmNavigationBarPreview() {
    val items = listOf("Home", "Properties", "Chats", "Profile")
    val icons = listOf(
        PmIcons.homeUnselected(),
        PmIcons.propertiesUnselected(),
        PmIcons.chatsUnselected(),
        PmIcons.profileUnselected()
    )

    val selectedIcons = listOf(
        PmIcons.homeSelected(),
        PmIcons.propertiesSelected(),
        PmIcons.chatsSelected(),
        PmIcons.profileSelected()
    )

    PmTheme {
        PmNavigationBar {
            items.forEachIndexed { index, item ->
                PmNavigationBarItem(
                    icon = {
                        Icon(
                            painter = icons[index],
                            contentDescription = item,
                        )
                    },
                    selectedIcon = {
                        Icon(
                            painter = selectedIcons[index],
                            contentDescription = item,
                        )
                    },
                    label = { Text(item) },
                    selected = index == 0,
                    onClick = {}
                )
            }
        }
    }
}
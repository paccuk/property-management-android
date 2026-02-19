package org.stkachenko.propertymanagement.core.designsystem

import androidx.activity.ComponentActivity
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.LooperMode
import org.stkachenko.propertymanagement.core.designsystem.component.PmNavigationBar
import org.stkachenko.propertymanagement.core.designsystem.component.PmNavigationBarItem
import org.stkachenko.propertymanagement.core.designsystem.icon.PmIcons
import org.stkachenko.propertymanagement.core.testing.util.captureMultiTheme

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = HiltTestApplication::class, qualifiers = "480dpi")
@LooperMode(LooperMode.Mode.PAUSED)
class NavigationScreenshotTests {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun pmNavigationBar_multipleThemes() {
        composeTestRule.captureMultiTheme("Navigation", "NavigationBar") { _ ->
            PmNavigationBar {
                PmNavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = PmIcons.PropertiesUnselected,
                            contentDescription = "Properties",
                        )
                    },
                    selectedIcon = {
                        Icon(
                            imageVector = PmIcons.PropertiesSelected,
                            contentDescription = "Properties",
                        )
                    },
                    label = { Text("Properties") },
                    selected = true,
                    onClick = {},
                )
                PmNavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = PmIcons.ChatsUnselected,
                            contentDescription = "Chats",
                        )
                    },
                    selectedIcon = {
                        Icon(
                            imageVector = PmIcons.ChatsSelected,
                            contentDescription = "Chats",
                        )
                    },
                    label = { Text("Chats") },
                    selected = false,
                    onClick = {},
                )
                PmNavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = PmIcons.ProfileUnselected,
                            contentDescription = "Profile",
                        )
                    },
                    selectedIcon = {
                        Icon(
                            imageVector = PmIcons.ProfileSelected,
                            contentDescription = "Profile",
                        )
                    },
                    label = { Text("Profile") },
                    selected = false,
                    onClick = {},
                )
            }
        }
    }

    @Test
    fun pmNavigationBarItem_selected_multipleThemes() {
        composeTestRule.captureMultiTheme("Navigation", "NavigationBarItemSelected") { _ ->
            PmNavigationBar {
                PmNavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = PmIcons.PropertiesUnselected,
                            contentDescription = "Properties",
                        )
                    },
                    selectedIcon = {
                        Icon(
                            imageVector = PmIcons.PropertiesSelected,
                            contentDescription = "Properties",
                        )
                    },
                    label = { Text("Properties") },
                    selected = true,
                    onClick = {},
                )
            }
        }
    }

    @Test
    fun pmNavigationBarItem_unselected_multipleThemes() {
        composeTestRule.captureMultiTheme("Navigation", "NavigationBarItemUnselected") { _ ->
            PmNavigationBar {
                PmNavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = PmIcons.PropertiesUnselected,
                            contentDescription = "Properties",
                        )
                    },
                    selectedIcon = {
                        Icon(
                            imageVector = PmIcons.PropertiesSelected,
                            contentDescription = "Properties",
                        )
                    },
                    label = { Text("Properties") },
                    selected = false,
                    onClick = {},
                )
            }
        }
    }
}


package org.stkachenko.propertymanagement.core.designsystem

import androidx.activity.ComponentActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.LooperMode
import org.stkachenko.propertymanagement.core.designsystem.component.PmTopAppBar
import org.stkachenko.propertymanagement.core.testing.util.captureMultiTheme

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = HiltTestApplication::class, qualifiers = "480dpi")
@LooperMode(LooperMode.Mode.PAUSED)
class TopAppBarScreenshotTests {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun pmTopAppBar_multipleThemes() {
        composeTestRule.captureMultiTheme("TopAppBar") { _ ->
            PmTopAppBar(
                titleRes = android.R.string.untitled,
                actionIcon = Icons.Default.Notifications,
                actionIconContentDescription = "Notifications",
            )
        }
    }
}


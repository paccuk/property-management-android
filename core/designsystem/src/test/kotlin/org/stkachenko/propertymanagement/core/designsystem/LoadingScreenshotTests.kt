package org.stkachenko.propertymanagement.core.designsystem

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.unit.dp
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.LooperMode
import org.stkachenko.propertymanagement.core.designsystem.component.PmLoadingWheel
import org.stkachenko.propertymanagement.core.designsystem.component.PmOverlayLoadingWheel
import org.stkachenko.propertymanagement.core.testing.util.captureMultiTheme

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = HiltTestApplication::class, qualifiers = "480dpi")
@LooperMode(LooperMode.Mode.PAUSED)
class LoadingScreenshotTests {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun pmLoadingWheel_multipleThemes() {
        composeTestRule.captureMultiTheme("Loading", "LoadingWheel") { _ ->
            Surface(modifier = Modifier.size(80.dp)) {
                PmLoadingWheel(contentDesc = "Loading")
            }
        }
    }

    @Test
    fun pmOverlayLoadingWheel_multipleThemes() {
        composeTestRule.captureMultiTheme("Loading", "OverlayLoadingWheel") { _ ->
            Surface(modifier = Modifier.size(80.dp)) {
                PmOverlayLoadingWheel(contentDesc = "Loading")
            }
        }
    }
}


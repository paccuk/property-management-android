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
import org.stkachenko.propertymanagement.core.designsystem.component.PmBuildingHouseLoading
import org.stkachenko.propertymanagement.core.designsystem.component.PmOverlayBuildingHouseLoading
import org.stkachenko.propertymanagement.core.testing.util.captureMultiTheme

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = HiltTestApplication::class, qualifiers = "480dpi")
@LooperMode(LooperMode.Mode.PAUSED)
class BuildingHouseLoadingScreenshotTests {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun pmBuildingHouseLoading_multipleThemes() {
        composeTestRule.captureMultiTheme("BuildingHouseLoading", "BuildingHouseLoading") { _ ->
            Surface(modifier = Modifier.size(80.dp)) {
                PmBuildingHouseLoading(contentDesc = "Loading")
            }
        }
    }

    @Test
    fun pmOverlayBuildingHouseLoading_multipleThemes() {
        composeTestRule.captureMultiTheme(
            "BuildingHouseLoading",
            "OverlayBuildingHouseLoading"
        ) { _ ->
            Surface(modifier = Modifier.size(80.dp)) {
                PmOverlayBuildingHouseLoading(contentDesc = "Loading")
            }
        }
    }
}


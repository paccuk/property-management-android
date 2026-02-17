package org.stkachenko.propertymanagement.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavHostController
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.TimeZone
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.stkachenko.propertymanagement.core.designsystem.theme.backgroundLight
import org.stkachenko.propertymanagement.core.testing.util.TestNetworkMonitor
import org.stkachenko.propertymanagement.core.testing.util.TestTimeZoneMonitor
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
@HiltAndroidTest
class PmAppStateTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val networkMonitor = TestNetworkMonitor()

    private val timeZoneMonitor = TestTimeZoneMonitor()

    private lateinit var state: PmAppState

    @Composable
    private fun testNavController() = NavHostController(LocalContext.current)

    @Test
    fun pmAppState_whenNetworkMonitorIsOffline_StateIsOffline() =
        runTest(UnconfinedTestDispatcher()) {
            composeTestRule.setContent {
                state = PmAppState(
                    coroutineScope = backgroundScope,
                    networkMonitor = networkMonitor,
                    timeZoneMonitor = timeZoneMonitor,
                    navController = testNavController(),
                )
            }

            backgroundScope.launch { state.isOffline.collect() }
            networkMonitor.setConnected(false)
            assertEquals(true, state.isOffline.value)
        }

    @Test
    fun pmAppState_differentTZ_withTimeZoneMonitorChange() = runTest(UnconfinedTestDispatcher()) {
        composeTestRule.setContent {
            state = PmAppState(
                coroutineScope = backgroundScope,
                networkMonitor = networkMonitor,
                timeZoneMonitor = timeZoneMonitor,
                navController = testNavController(),
            )
        }
        val changedTz = TimeZone.of("Europe/Prague")
        backgroundScope.launch { state.currentTimeZone.collect() }
        timeZoneMonitor.setTimeZone(changedTz)
        assertEquals(
            changedTz,
            state.currentTimeZone.value,
        )
    }
}

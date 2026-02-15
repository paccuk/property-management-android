package org.stkachenko.propertymanagement.ui

import androidx.compose.ui.test.junit4.createComposeRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
@HiltAndroidTest
class PmAppStateTest {

    @get:Rule
    val composeTestRule = createComposeRule()
}
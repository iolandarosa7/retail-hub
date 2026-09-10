/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.presentation.address

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runComposeUiTest
import com.iolandarosa.retailhub.core.ui.theme.RetailHubTheme
import com.iolandarosa.retailhub.features.auth.utils.TestUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class AddressScreenTest {
    private lateinit var scheduler: TestCoroutineScheduler
    private lateinit var dispatcher: CoroutineDispatcher

    @BeforeTest
    fun setup() {
        scheduler = TestCoroutineScheduler()
        dispatcher = StandardTestDispatcher(scheduler)
    }

    @Test
    fun success_screenLoaded_displayAddressData() =
        runComposeUiTest(runTestContext = dispatcher) {
            val address = TestUser.user.address

            setContent {
                RetailHubTheme {
                    AddressScreen(
                        paddingValues = PaddingValues(),
                        address = address,
                    )
                }
            }

            scheduler.advanceUntilIdle()

            onNodeWithText(address.street).assertIsDisplayed()
            onNodeWithText("${address.city}, ${address.stateCode} ${address.postalCode}").assertIsDisplayed()
            onNodeWithText("MAP").assertIsDisplayed()
            onNodeWithText("LOCATION").assertIsDisplayed()
            onNodeWithText(address.city).assertIsDisplayed()
            onNodeWithText("${address.state} · ${address.stateCode}").assertIsDisplayed()
            onNodeWithText(address.postalCode).assertIsDisplayed()
            onNodeWithText("COUNTRY").assertIsDisplayed()
            onNodeWithText(address.country).assertIsDisplayed()
            onNodeWithText("COORDINATES").assertIsDisplayed()
            onNodeWithText("${address.coordinates.lat}, ${address.coordinates.lng}").assertIsDisplayed()
            onNodeWithText("Open in Maps").assertIsDisplayed()
        }
}

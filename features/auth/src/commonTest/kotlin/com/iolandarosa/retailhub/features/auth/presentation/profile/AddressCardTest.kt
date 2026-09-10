/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.presentation.profile

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import com.iolandarosa.retailhub.features.auth.utils.TestUser
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class AddressCardTest {
    @Test
    fun componentLoaded_onClick_callsExpectedCallback() =
        runComposeUiTest {
            val address = TestUser.user.address
            var callbackCalled = false

            setContent {
                AddressCard(address = address, onClick = { callbackCalled = true })
            }

            onNodeWithText("${address.street}, ${address.city}, ${address.postalCode}").assertIsDisplayed()

            onNodeWithContentDescription("Address details")
                .assertIsEnabled()
                .performClick()

            waitUntil { callbackCalled }
        }

    @Test
    fun nullAddress_componentLoaded_hasExpectedUI() =
        runComposeUiTest {
            setContent { AddressCard() }

            onNodeWithContentDescription("Address details")
                .assertIsNotEnabled()
        }
}

/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.composeapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import retailhub.composeapp.generated.resources.Res
import retailhub.composeapp.generated.resources.address_details
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class RetailHubTopAppBarTest {
    private val config = AppBarConfig(Res.string.address_details, showBack = true)

    @Composable
    private fun TestRetailHubTopAppBar(
        appBarConfig: AppBarConfig = config,
        onBack: () -> Unit = {},
    ) = RetailHubTopAppBar(
        appBarConfig = appBarConfig,
        onBack = onBack,
    )

    @Test
    fun initialState_componentLoaded_hasExpectedUI() =
        runComposeUiTest {
            setContent { TestRetailHubTopAppBar() }

            onNodeWithText("Address details").assertIsDisplayed()
            onNodeWithContentDescription("Back")
                .assertIsDisplayed()
                .assertIsEnabled()
        }

    @Test
    fun componentLoaded_showBackFalse_hasExpectedUI() =
        runComposeUiTest {
            setContent { TestRetailHubTopAppBar(config.copy(showBack = false)) }

            onNodeWithText("Address details").assertIsDisplayed()
            onNodeWithContentDescription("Back")
                .assertIsNotDisplayed()
        }

    @Test
    fun componentLoaded_onBackClick_expectedCallbackCalled() =
        runComposeUiTest {
            var callbackCalled = false

            setContent { TestRetailHubTopAppBar(onBack = { callbackCalled = true }) }

            onNodeWithContentDescription("Back")
                .assertIsDisplayed()
                .performClick()

            waitUntil { callbackCalled }
        }
}

/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.presentation.address

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import app.cash.turbine.test
import com.iolandarosa.retailhub.core.common.clipboard.AppClipboardManager
import com.iolandarosa.retailhub.core.ui.theme.RetailHubTheme
import com.iolandarosa.retailhub.features.auth.TestDispatcherProvider
import com.iolandarosa.retailhub.features.auth.utils.TestUser
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class AddressScreenTest {
    private lateinit var scheduler: TestCoroutineScheduler
    private lateinit var dispatcher: CoroutineDispatcher
    private lateinit var viewModel: AddressViewModel
    private val clipboardManager: AppClipboardManager = mock()

    private val address = TestUser.user.address

    @BeforeTest
    fun setup() {
        scheduler = TestCoroutineScheduler()
        dispatcher = StandardTestDispatcher(scheduler)
        viewModel =
            AddressViewModel(
                address = address,
                clipboardManager = clipboardManager,
                dispatcherProvider = TestDispatcherProvider(dispatcher),
            )
    }

    @Composable
    fun TestAddressScreen(onShowMessage: (String) -> Unit = {}) =
        AddressScreen(
            paddingValues = PaddingValues(),
            onShowMessage = onShowMessage,
            viewModel = viewModel,
        )

    @Test
    fun initialState_screenLoaded_displayAddressData() =
        runComposeUiTest {
            setContent {
                RetailHubTheme { TestAddressScreen() }
            }

            onNodeWithText(address.street).assertIsDisplayed()
            onNodeWithText("${address.city}, ${address.stateCode} ${address.postalCode}").assertIsDisplayed()
            onNodeWithContentDescription("Map image of user location").assertIsDisplayed()
            onNodeWithText("LOCATION").assertIsDisplayed()
            onNodeWithText(address.city).assertIsDisplayed()
            onNodeWithText("${address.state} · ${address.stateCode}").assertIsDisplayed()
            onNodeWithText("COUNTRY").assertIsDisplayed()
            onNodeWithText(address.country).assertIsDisplayed()
            onNodeWithText("COORDINATES").assertIsDisplayed()
            onNodeWithText("${address.coordinates.lat}, ${address.coordinates.lng}").assertIsDisplayed()
            onNodeWithText("Open in Maps").assertIsDisplayed()
        }

    @Test
    fun withPlatformFeedbackProvided_copyClipboard_expectViewModelCalled() =
        runComposeUiTest(runTestContext = dispatcher) {
            every { clipboardManager.copy(any()) } returns Unit
            every { clipboardManager.providesFeedback } returns true

            setContent {
                RetailHubTheme { TestAddressScreen() }
            }

            onNodeWithContentDescription("Copy to clipboard")
                .performClick()

            scheduler.advanceUntilIdle()

            verify { clipboardManager.copy("${address.coordinates.lat}, ${address.coordinates.lng}") }
        }

    @Test
    fun withoutPlatformFeedbackProvided_copyClipboard_expectCallbackCalled() =
        runComposeUiTest(runTestContext = dispatcher) {
            every { clipboardManager.copy(any()) } returns Unit
            every { clipboardManager.providesFeedback } returns false

            var clipboardText: String? = null

            setContent {
                RetailHubTheme {
                    TestAddressScreen(
                        onShowMessage = { clipboardText = it },
                    )
                }
            }

            onNodeWithContentDescription("Copy to clipboard")
                .performClick()

            scheduler.advanceUntilIdle()

            verify { clipboardManager.copy("${address.coordinates.lat}, ${address.coordinates.lng}") }

            viewModel.effects.test {
                awaitIdle()
            }

            waitUntil { clipboardText == "Copied to clipboard" }
        }
}

/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.presentation.address

import app.cash.turbine.test
import com.iolandarosa.retailhub.core.common.clipboard.AppClipboardManager
import com.iolandarosa.retailhub.features.auth.TestDispatcherProvider
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class AddressViewModelTest {
    private val clipboardManager = mock<AppClipboardManager>()
    private val scheduler = TestCoroutineScheduler()
    private val dispatcher = StandardTestDispatcher(scheduler)
    private lateinit var viewModel: AddressViewModel

    @BeforeTest
    fun setup() {
        viewModel =
            AddressViewModel(
                dispatcherProvider = TestDispatcherProvider(dispatcher),
                clipboardManager = clipboardManager,
            )
    }

    @Test
    fun onClipboardCopy_whenPlatformDoesNotProvideFeedback_emitsSuccessEffect() =
        runTest(scheduler) {
            val textToCopy = "1.0, 2.0"
            every { clipboardManager.providesFeedback } returns false
            every { clipboardManager.copy(textToCopy) } returns Unit

            viewModel.onIntent(AddressContract.Intent.OnClipboardCopy(textToCopy))

            advanceUntilIdle()

            viewModel.effects.test {
                assertEquals(AddressContract.Effect.ShowCopySuccess, awaitItem())
            }

            verify {
                clipboardManager.copy(textToCopy)
            }
        }

    @Test
    fun onClipboardCopy_whenPlatformProvidesFeedback_doesNotEmitEffect() =
        runTest(scheduler) {
            val textToCopy = "1.0, 2.0"
            every { clipboardManager.providesFeedback } returns true
            every { clipboardManager.copy(textToCopy) } returns Unit

            viewModel.onIntent(AddressContract.Intent.OnClipboardCopy(textToCopy))

            advanceUntilIdle()

            viewModel.effects.test {
                expectNoEvents()
            }

            verify {
                clipboardManager.copy(textToCopy)
            }
        }
}

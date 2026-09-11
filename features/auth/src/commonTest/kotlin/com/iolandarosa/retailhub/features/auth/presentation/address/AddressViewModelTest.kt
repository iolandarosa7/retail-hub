/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.presentation.address

import app.cash.turbine.test
import com.iolandarosa.retailhub.core.common.clipboard.AppClipboardManager
import com.iolandarosa.retailhub.core.common.maps.MapManager
import com.iolandarosa.retailhub.features.auth.TestDispatcherProvider
import com.iolandarosa.retailhub.features.auth.utils.TestUser
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
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
    private val mapManager: MapManager = mock()
    private val mapUrl = "https://map.com"

    @BeforeTest
    fun setup() {
        every { mapManager.getStaticMapUrl(any(), any()) } returns mapUrl
        viewModel =
            AddressViewModel(
                address = TestUser.user.address,
                dispatcherProvider = TestDispatcherProvider(dispatcher),
                clipboardManager = clipboardManager,
                mapManager = mapManager,
            )
    }

    @Test
    fun initialState_viewModelInstance_hasExpectedValues() {
        assertEquals(TestUser.user.address, viewModel.state.value.address)
        assertEquals(mapUrl, viewModel.state.value.staticMapUrl)
        assertEquals(
            "${TestUser.user.address.coordinates.lat}, ${TestUser.user.address.coordinates.lng}",
            viewModel.state.value.coordinatesStr,
        )
        verify {
            mapManager.getStaticMapUrl(
                TestUser.user.address.coordinates.lat,
                TestUser.user.address.coordinates.lng,
            )
        }
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

    @Test
    fun initialState_openMap_callsExpectedMethod() {
        every { mapManager.openMap(any(), any(), any()) } returns Unit

        viewModel.onIntent(AddressContract.Intent.OpenMap)

        verify {
            mapManager.openMap(
                lat = TestUser.user.address.coordinates.lat,
                lng = TestUser.user.address.coordinates.lng,
                label = TestUser.user.address.street,
            )
        }
    }
}

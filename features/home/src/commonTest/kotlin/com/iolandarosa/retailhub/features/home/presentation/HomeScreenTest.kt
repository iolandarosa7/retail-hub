/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.home.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import app.cash.turbine.test
import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.core.user.domain.interactors.GetAuthUserUseCase
import com.iolandarosa.retailhub.core.user.domain.interactors.GetLocalUserImageUseCase
import com.iolandarosa.retailhub.features.home.TestDispatcherProvider
import com.iolandarosa.retailhub.features.home.utils.TestUser
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class HomeScreenTest {
    private val getAuthUserUseCase = mock<GetAuthUserUseCase>()
    private val getLocalUserImageUseCase = mock<GetLocalUserImageUseCase>()
    private lateinit var scheduler: TestCoroutineScheduler
    private lateinit var dispatcher: CoroutineDispatcher
    private lateinit var viewModel: HomeViewModel

    @BeforeTest
    fun setup() {
        scheduler = TestCoroutineScheduler()
        dispatcher = StandardTestDispatcher(scheduler)

        viewModel =
            HomeViewModel(
                getAuthUserUseCase = getAuthUserUseCase,
                dispatcherProvider = TestDispatcherProvider(dispatcher),
                getLocalUserImageUseCase = getLocalUserImageUseCase,
            )
    }

    @Composable
    private fun TestHomeScreen(navigateToProfile: () -> Unit = {}) =
        HomeScreen(
            paddingValues = PaddingValues(),
            viewModel = viewModel,
            navigateToProfile = navigateToProfile,
        )

    @Test
    fun success_screenLoaded_displaysExpectedUI() =
        runComposeUiTest(runTestContext = dispatcher) {
            val user = TestUser.user

            everySuspend { getAuthUserUseCase() } returns NetworkResult.Success(user)
            everySuspend { getLocalUserImageUseCase(user.id) } returns null

            setContent { TestHomeScreen() }

            scheduler.advanceUntilIdle()

            onNodeWithText("Retail Hub").assertIsDisplayed()
            onNodeWithContentDescription("Go to profile").assertIsDisplayed().assertIsEnabled()
        }

    @Test
    fun success_onProfileClick_expectedCallbackCalled() =
        runComposeUiTest(runTestContext = dispatcher) {
            val user = TestUser.user
            var callbackCalled = false

            everySuspend { getAuthUserUseCase() } returns NetworkResult.Success(user)
            everySuspend { getLocalUserImageUseCase(user.id) } returns byteArrayOf(1)

            setContent { TestHomeScreen { callbackCalled = true } }

            scheduler.advanceUntilIdle()

            onNodeWithContentDescription("Go to profile").performClick()

            viewModel.effects.test {
                awaitIdle()
            }

            waitUntil { callbackCalled }
        }
}

/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.presentation.profile

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runComposeUiTest
import app.cash.turbine.test
import com.iolandarosa.retailhub.core.model.ApiErrorResponse
import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.features.auth.TestDispatcherProvider
import com.iolandarosa.retailhub.features.auth.domain.interactors.GetAuthUserUseCase
import com.iolandarosa.retailhub.features.auth.utils.TestUser
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class ProfileScreenTest {
    private val getAuthUserUseCase = mock<GetAuthUserUseCase>()

    private lateinit var scheduler: TestCoroutineScheduler
    private lateinit var dispatcher: CoroutineDispatcher
    private lateinit var viewModel: ProfileViewModel

    @BeforeTest
    fun setup() {
        scheduler = TestCoroutineScheduler()
        dispatcher = StandardTestDispatcher(scheduler)

        viewModel =
            ProfileViewModel(
                getAuthUserUseCase = getAuthUserUseCase,
                dispatcherProvider = TestDispatcherProvider(dispatcher),
            )
    }

    @Test
    fun success_screenLoaded_displayUserData() =
        runComposeUiTest(runTestContext = dispatcher) {
            val user = TestUser.user

            everySuspend { getAuthUserUseCase() } returns NetworkResult.Success(user)

            setContent {
                ProfileScreen(
                    paddingValues = PaddingValues(),
                    navigateToLogin = {},
                    viewModel = viewModel,
                )
            }

            scheduler.advanceUntilIdle()

            onNodeWithText(user.name).assertIsDisplayed()
            onNodeWithText(user.role).assertIsDisplayed()
            onNodeWithText(user.email).assertIsDisplayed()
        }

    @Test
    fun errorUnauthorized_screenLoaded_expectCallbackCalled() =
        runComposeUiTest(runTestContext = dispatcher) {
            everySuspend { getAuthUserUseCase() } returns NetworkResult.Failure.Unauthorized

            var callbackCalled = false

            setContent {
                ProfileScreen(
                    paddingValues = PaddingValues(),
                    navigateToLogin = { callbackCalled = true },
                    viewModel = viewModel,
                )
            }

            viewModel.effects.test {
                awaitIdle()
                assertTrue(callbackCalled)
            }
        }

    @Test
    fun error_screenLoaded_displaysErrorMessage() =
        runComposeUiTest(runTestContext = dispatcher) {
            val errorMessage = "Error message"
            everySuspend { getAuthUserUseCase() } returns NetworkResult.Failure.ApiError(ApiErrorResponse(errorMessage))

            setContent {
                ProfileScreen(
                    paddingValues = PaddingValues(),
                    navigateToLogin = {},
                    viewModel = viewModel,
                )
            }

            scheduler.advanceUntilIdle()

            onNodeWithText(errorMessage).assertIsDisplayed()
        }
}

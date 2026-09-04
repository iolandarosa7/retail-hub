/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.presentation.login

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.v2.runComposeUiTest
import com.iolandarosa.retailhub.core.model.ApiErrorResponse
import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.features.auth.TestDispatcherProvider
import com.iolandarosa.retailhub.features.auth.domain.interactors.LoginUseCase
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class LoginScreenTest {
    private val loginUseCase = mock<LoginUseCase>()

    private lateinit var scheduler: TestCoroutineScheduler
    private lateinit var dispatcher: CoroutineDispatcher
    private lateinit var viewModel: LoginViewModel

    @BeforeTest
    fun setup() {
        scheduler = TestCoroutineScheduler()
        dispatcher = StandardTestDispatcher(scheduler)

        viewModel =
            LoginViewModel(
                loginUseCase = loginUseCase,
                dispatcherProvider = TestDispatcherProvider(dispatcher),
            )
    }

    @Test
    fun loginScreenDisplaysCorrectly() =
        runComposeUiTest(runTestContext = dispatcher) {
            setContent {
                LoginScreen(
                    paddingValues = PaddingValues(),
                    viewModel = viewModel,
                    navigateToProfile = {},
                )
            }

            onNodeWithText("Username")
                .assertIsDisplayed()
                .assertIsEnabled()

            onNodeWithText("Password")
                .assertIsDisplayed()
                .assertIsEnabled()

            onNodeWithText("Sign in")
                .assertIsDisplayed()
                .assertIsEnabled()
        }

    @Test
    fun clickingLoginWithInvalidFormDoesNothing() =
        runComposeUiTest(runTestContext = dispatcher) {
            setContent {
                LoginScreen(
                    paddingValues = PaddingValues(),
                    viewModel = viewModel,
                    navigateToProfile = {},
                )
            }

            onNodeWithText("Sign in").performClick()

            scheduler.advanceUntilIdle()

            assertEquals(
                LoginRequestState.Initial,
                viewModel.state.value.loginRequest,
            )

            onAllNodesWithText("Required field")[0].assertIsDisplayed()
        }

    @Test
    fun clickingLoginShowsLoadingStateAndIfSuccessCallCallback() =
        runComposeUiTest(runTestContext = dispatcher) {
            var navigateToProfile = false

            everySuspend { loginUseCase(any(), any()) } returns
                NetworkResult.Success(Unit)

            setContent {
                LoginScreen(
                    paddingValues = PaddingValues(),
                    viewModel = viewModel,
                    navigateToProfile = { navigateToProfile = true },
                )
            }

            onNodeWithText("Username").performTextInput("username")
            onNodeWithText("Password").performTextInput("password")
            onNodeWithText("Sign in").performClick()

            assertEquals(
                LoginRequestState.Loading,
                viewModel.state.value.loginRequest,
            )

            onNodeWithText("Sign in").assertIsNotEnabled()
            onNodeWithText("Username").assertIsNotEnabled()
            onNodeWithText("Password").assertIsNotEnabled()

            scheduler.advanceUntilIdle()

            assertEquals(
                LoginRequestState.Success,
                viewModel.state.value.loginRequest,
            )

            onNodeWithText("Sign in").assertIsEnabled()
            onNodeWithText("Username").assertIsEnabled()
            onNodeWithText("Password").assertIsEnabled()

            assertTrue(navigateToProfile)
        }

    @Test
    fun failedLoginDisplaysError() =
        runComposeUiTest(runTestContext = dispatcher) {
            val errorMessage = "error message"
            everySuspend { loginUseCase(username = any(), password = any()) } returns
                NetworkResult.Failure.ApiError(ApiErrorResponse(errorMessage))

            setContent {
                LoginScreen(
                    paddingValues = PaddingValues(),
                    viewModel = viewModel,
                    navigateToProfile = {},
                )
            }

            onNodeWithText("Username").performTextInput("username")
            onNodeWithText("Password").performTextInput("password")
            onNodeWithText("Sign in").performClick()

            assertEquals(
                LoginRequestState.Loading,
                viewModel.state.value.loginRequest,
            )

            scheduler.advanceUntilIdle()

            assertIs<LoginRequestState.Error>(viewModel.state.value.loginRequest)

            onNodeWithText(errorMessage)
                .assertIsDisplayed()
        }
}

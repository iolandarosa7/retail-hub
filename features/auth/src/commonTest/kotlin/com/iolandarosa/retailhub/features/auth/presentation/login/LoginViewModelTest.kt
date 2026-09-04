/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.presentation.login

import app.cash.turbine.test
import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.core.ui.error.UiError
import com.iolandarosa.retailhub.core.ui.form.fields.TextFormField
import com.iolandarosa.retailhub.features.auth.TestDispatcherProvider
import com.iolandarosa.retailhub.features.auth.domain.interactors.LoginUseCase
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    private val loginUseCase = mock<LoginUseCase>()
    private val scheduler = TestCoroutineScheduler()
    private val dispatcher = StandardTestDispatcher(scheduler)

    private lateinit var viewModel: LoginViewModel

    @BeforeTest
    fun setup() {
        viewModel =
            LoginViewModel(
                loginUseCase = loginUseCase,
                dispatcherProvider = TestDispatcherProvider(dispatcher),
            )
    }

    @Test
    fun initialStateIsCorrect() =
        runTest {
            assertEquals(LoginRequestState.Initial, viewModel.state.value.loginRequest)

            assertTrue(viewModel.state.value.isInteractionEnabled)

            assertNotNull(viewModel.state.value.formState)

            val fields = viewModel.state.value.formState.fields

            assertEquals(2, fields.size)

            assertEquals(LoginForm.USERNAME, fields[0].name)
            assertEquals(LoginForm.PASSWORD, fields[1].name)
        }

    @Test
    fun onLoginClickDoesNothingWhenFormIsInvalid() =
        runTest(scheduler) {
            viewModel.onIntent(LoginIntent.OnLoginClicked)

            advanceUntilIdle()

            assertEquals(
                LoginRequestState.Initial,
                viewModel.state.value.loginRequest,
            )

            verifySuspend(VerifyMode.not) {
                loginUseCase(any(), any())
            }
        }

    @Test
    fun onLoginSuccessChangesStateToSuccess() =
        runTest(scheduler) {
            val username = "username"
            val password = "password"

            everySuspend {
                loginUseCase(any(), any())
            } returns NetworkResult.Success(Unit)

            setFieldValue(0, username)
            setFieldValue(1, password)

            viewModel.onIntent(LoginIntent.OnLoginClicked)

            assertEquals(LoginRequestState.Loading, viewModel.state.value.loginRequest)

            assertFalse(viewModel.state.value.isInteractionEnabled)

            assertNull(viewModel.state.value.error)

            advanceUntilIdle()

            assertEquals(LoginRequestState.Success, viewModel.state.value.loginRequest)

            assertTrue(viewModel.state.value.isInteractionEnabled)

            verifySuspend { loginUseCase(username, password) }

            viewModel.effects.test {
                assertEquals(LoginEffect.NavigateToProfile, awaitItem())
            }
        }

    @Test
    fun onLoginErrorChangesStateToError() =
        runTest(scheduler) {
            val username = "username"
            val password = "password"

            val failure = NetworkResult.Failure.Unknown()

            everySuspend { loginUseCase(any(), any()) } returns failure

            setFieldValue(0, username)
            setFieldValue(1, password)

            viewModel.onIntent(LoginIntent.OnLoginClicked)

            assertFalse(viewModel.state.value.isInteractionEnabled)

            assertNull(viewModel.state.value.error)

            advanceUntilIdle()

            assertIs<LoginRequestState.Error>(viewModel.state.value.loginRequest)

            assertTrue(viewModel.state.value.isInteractionEnabled)

            assertEquals(UiError(), viewModel.state.value.error)

            verifySuspend { loginUseCase(username, password) }
        }

    @Test
    fun formChangeResetsError() =
        runTest(scheduler) {
            everySuspend {
                loginUseCase(any(), any())
            } returns NetworkResult.Failure.Unauthorized

            setFieldValue(0, "username")
            setFieldValue(1, "password")

            viewModel.onIntent(LoginIntent.OnLoginClicked)

            advanceUntilIdle()

            assertIs<LoginRequestState.Error>(viewModel.state.value.loginRequest)

            viewModel.onIntent(LoginIntent.OnFormFieldChanged)

            assertEquals(LoginRequestState.Initial, viewModel.state.value.loginRequest)
        }

    private fun setFieldValue(
        index: Int,
        value: String,
    ) {
        (viewModel.state.value.formState.fields[index] as TextFormField).value = value
    }
}

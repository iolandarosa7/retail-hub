/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.presentation.profile

import app.cash.turbine.test
import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.features.auth.TestDispatcherProvider
import com.iolandarosa.retailhub.features.auth.domain.interactors.GetAuthUserUseCase
import com.iolandarosa.retailhub.features.auth.domain.interactors.LogoutUseCase
import com.iolandarosa.retailhub.features.auth.presentation.profile.ProfileContract.Effect
import com.iolandarosa.retailhub.features.auth.presentation.profile.ProfileContract.Intent
import com.iolandarosa.retailhub.features.auth.presentation.profile.ProfileContract.LogoutRequestState
import com.iolandarosa.retailhub.features.auth.presentation.profile.ProfileContract.UserRequestState
import com.iolandarosa.retailhub.features.auth.utils.TestUser
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
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
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {
    private val getAuthUserUseCase = mock<GetAuthUserUseCase>()
    private val logoutUseCase = mock<LogoutUseCase>()
    private val scheduler = TestCoroutineScheduler()
    private val dispatcher = StandardTestDispatcher(scheduler)
    private lateinit var viewModel: ProfileViewModel

    @BeforeTest
    fun setup() {
        viewModel =
            ProfileViewModel(
                getAuthUserUseCase,
                logoutUseCase,
                dispatcherProvider = TestDispatcherProvider(dispatcher),
            )
    }

    @Test
    fun initialInstance_hasExpectedState() {
        assertEquals(UserRequestState.Initial, viewModel.state.value.userRequest)
        assertEquals(LogoutRequestState.Initial, viewModel.state.value.logoutRequest)
        assertFalse(viewModel.state.value.isRefreshing)
    }

    @Test
    fun success_loadProfile_hasExpectedState() =
        runTest(scheduler) {
            val data = TestUser.user
            everySuspend { getAuthUserUseCase() } returns NetworkResult.Success(data)

            viewModel.onIntent(Intent.LoadProfile)

            assertEquals(UserRequestState.Loading, viewModel.state.value.userRequest)

            advanceUntilIdle()

            assertEquals(UserRequestState.Success(data), viewModel.state.value.userRequest)

            verifySuspend { getAuthUserUseCase() }
        }

    @Test
    fun success_refreshProfile_hasExpectedState() =
        runTest(scheduler) {
            val data = TestUser.user
            everySuspend { getAuthUserUseCase() } returns NetworkResult.Success(data)

            viewModel.onIntent(Intent.RefreshProfile)

            assertEquals(UserRequestState.Initial, viewModel.state.value.userRequest)
            assertTrue(viewModel.state.value.isRefreshing)

            advanceUntilIdle()

            assertEquals(UserRequestState.Success(data), viewModel.state.value.userRequest)
            assertFalse(viewModel.state.value.isRefreshing)

            verifySuspend { getAuthUserUseCase() }
        }

    @Test
    fun errorUnauthorized_loadProfile_hasExpectedStateAndEffect() =
        runTest(scheduler) {
            everySuspend { getAuthUserUseCase() } returns NetworkResult.Failure.Unauthorized

            viewModel.onIntent(Intent.LoadProfile)

            assertEquals(UserRequestState.Loading, viewModel.state.value.userRequest)

            advanceUntilIdle()

            viewModel.effects.test {
                assertEquals(Effect.NavigateToLogin, awaitItem())
            }

            assertIs<UserRequestState.Initial>(viewModel.state.value.userRequest)

            verifySuspend { getAuthUserUseCase() }
        }

    @Test
    fun errorUnauthorized_refreshProfile_hasExpectedStateAndEffect() =
        runTest(scheduler) {
            everySuspend { getAuthUserUseCase() } returns NetworkResult.Failure.Unauthorized

            viewModel.onIntent(Intent.RefreshProfile)

            assertEquals(UserRequestState.Initial, viewModel.state.value.userRequest)
            assertTrue(viewModel.state.value.isRefreshing)

            advanceUntilIdle()

            viewModel.effects.test {
                assertEquals(Effect.NavigateToLogin, awaitItem())
            }

            assertIs<UserRequestState.Initial>(viewModel.state.value.userRequest)
            assertFalse(viewModel.state.value.isRefreshing)

            verifySuspend { getAuthUserUseCase() }
        }

    @Test
    fun error_loadProfile_hasExpectedState() =
        runTest(scheduler) {
            val failure = NetworkResult.Failure.Unknown()

            everySuspend { getAuthUserUseCase() } returns failure

            viewModel.onIntent(Intent.LoadProfile)

            assertEquals(UserRequestState.Loading, viewModel.state.value.userRequest)

            advanceUntilIdle()

            assertIs<UserRequestState.Error>(viewModel.state.value.userRequest)

            verifySuspend { getAuthUserUseCase() }
        }

    @Test
    fun error_refreshProfile_hasExpectedState() =
        runTest(scheduler) {
            val failure = NetworkResult.Failure.Unknown()

            everySuspend { getAuthUserUseCase() } returns failure

            viewModel.onIntent(Intent.RefreshProfile)

            assertEquals(UserRequestState.Initial, viewModel.state.value.userRequest)
            assertTrue(viewModel.state.value.isRefreshing)

            advanceUntilIdle()

            assertIs<UserRequestState.Error>(viewModel.state.value.userRequest)
            assertFalse(viewModel.state.value.isRefreshing)

            verifySuspend { getAuthUserUseCase() }
        }

    @Test
    fun success_logout_hasExpectedState() =
        runTest(scheduler) {
            everySuspend { logoutUseCase() } returns Unit

            viewModel.onIntent(Intent.Logout)

            assertEquals(LogoutRequestState.Loading, viewModel.state.value.logoutRequest)

            advanceUntilIdle()

            assertEquals(LogoutRequestState.Initial, viewModel.state.value.logoutRequest)

            viewModel.effects.test {
                assertEquals(Effect.NavigateToLogin, awaitItem())
            }

            verifySuspend { logoutUseCase() }
        }

    @Test
    fun viewAddressDetails_hasExpectedEffect() =
        runTest(scheduler) {
            val address = TestUser.user.address
            viewModel.onIntent(Intent.ViewAddressDetails(address))

            viewModel.effects.test {
                assertEquals(Effect.NavigateToAddressDetails(address), awaitItem())
            }
        }
}

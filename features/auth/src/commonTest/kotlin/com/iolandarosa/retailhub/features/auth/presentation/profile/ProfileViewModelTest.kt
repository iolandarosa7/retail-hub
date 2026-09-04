/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.presentation.profile

import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.features.auth.TestDispatcherProvider
import com.iolandarosa.retailhub.features.auth.domain.interactors.GetAuthUserUseCase
import com.iolandarosa.retailhub.features.auth.domain.model.User
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
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {
    private val getAuthUserUseCase = mock<GetAuthUserUseCase>()
    private val scheduler = TestCoroutineScheduler()
    private val dispatcher = StandardTestDispatcher(scheduler)
    private lateinit var viewModel: ProfileViewModel

    @BeforeTest
    fun setup() {
        viewModel =
            ProfileViewModel(
                getAuthUserUseCase,
                dispatcherProvider = TestDispatcherProvider(dispatcher),
            )
    }

    @Test
    fun initialInstance_hasExpectedState() {
        assertEquals(UserRequestState.Initial, viewModel.state.value.userRequest)
    }

    @Test
    fun success_loadProfile_hasExpectedState() =
        runTest(scheduler) {
            val data = User(id = 1, name = "name")
            everySuspend { getAuthUserUseCase() } returns NetworkResult.Success(data)

            viewModel.onIntent(ProfileIntent.LoadProfile)

            assertEquals(UserRequestState.Loading, viewModel.state.value.userRequest)

            advanceUntilIdle()

            assertEquals(UserRequestState.Success(data), viewModel.state.value.userRequest)

            verifySuspend { getAuthUserUseCase() }
        }

    @Test
    fun error_loadProfile_hasExpectedState() =
        runTest(scheduler) {
            val failure = NetworkResult.Failure.Unknown()

            everySuspend { getAuthUserUseCase() } returns failure

            viewModel.onIntent(ProfileIntent.LoadProfile)

            assertEquals(UserRequestState.Loading, viewModel.state.value.userRequest)

            advanceUntilIdle()

            assertIs<UserRequestState.Error>(viewModel.state.value.userRequest)

            verifySuspend { getAuthUserUseCase() }
        }
}

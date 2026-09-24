/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.home.presentation

import app.cash.turbine.test
import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.core.user.domain.interactors.GetAuthUserUseCase
import com.iolandarosa.retailhub.core.user.domain.interactors.GetLocalUserImageUseCase
import com.iolandarosa.retailhub.core.user.domain.model.Address
import com.iolandarosa.retailhub.core.user.domain.model.Coordinates
import com.iolandarosa.retailhub.core.user.domain.model.User
import com.iolandarosa.retailhub.features.home.TestDispatcherProvider
import com.iolandarosa.retailhub.features.home.domain.model.AuthUserImage
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

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    private val getAuthUserUseCase = mock<GetAuthUserUseCase>()
    private val getLocalUserImageUseCase = mock<GetLocalUserImageUseCase>()
    private val scheduler = TestCoroutineScheduler()
    private val dispatcher = StandardTestDispatcher(scheduler)
    private lateinit var viewModel: HomeViewModel

    private val testUser =
        User(
            id = 1,
            name = "John Doe",
            image = "https://example.com/user.png",
            role = "admin",
            email = "john@example.com",
            phone = "123456",
            age = 30,
            gender = "male",
            birthDate = "2000-01-01",
            bloodGroup = "A+",
            height = 180.0,
            weight = 80.0,
            eyeColor = "brown",
            hairColor = "black",
            hairType = "straight",
            address =
                Address(
                    street = "street",
                    city = "city",
                    state = "state",
                    stateCode = "stateCode",
                    postalCode = "postalCode",
                    coordinates = Coordinates(lat = 1.0, lng = 1.0),
                    country = "country",
                ),
        )

    @BeforeTest
    fun setup() {
        viewModel =
            HomeViewModel(
                getAuthUserUseCase = getAuthUserUseCase,
                dispatcherProvider = TestDispatcherProvider(dispatcher),
                getLocalUserImageUseCase = getLocalUserImageUseCase,
            )
    }

    @Test
    fun initialInstance_hasExpectedState() {
        assertEquals(AuthUserImage(), viewModel.state.value.authUserImage)
    }

    @Test
    fun onProfileClick_emitsNavigateUserProfileEffect() =
        runTest(scheduler) {
            viewModel.onIntent(HomeContract.Intent.OnProfileClick)

            advanceUntilIdle()

            viewModel.effects.test {
                assertEquals(HomeContract.Effect.NavigateUserProfile, awaitItem())
            }
        }

    @Test
    fun loadAuthUserImage_success_updatesAuthUserImageState() =
        runTest(scheduler) {
            val imageBytes = byteArrayOf(1, 2, 3)

            everySuspend { getAuthUserUseCase() } returns NetworkResult.Success(testUser)
            everySuspend { getLocalUserImageUseCase(testUser.id) } returns imageBytes

            viewModel.onIntent(HomeContract.Intent.LoadAuthUserImage)

            advanceUntilIdle()

            val expected =
                AuthUserImage(
                    url = testUser.image,
                    bytes = imageBytes,
                )

            assertEquals(expected, viewModel.state.value.authUserImage)

            verifySuspend {
                getAuthUserUseCase()
                getLocalUserImageUseCase(testUser.id)
            }
        }

    @Test
    fun loadAuthUserImage_failure_doesNotUpdateState() =
        runTest(scheduler) {
            everySuspend { getAuthUserUseCase() } returns NetworkResult.Failure.Unknown()

            viewModel.onIntent(HomeContract.Intent.LoadAuthUserImage)

            advanceUntilIdle()

            assertEquals(AuthUserImage(), viewModel.state.value.authUserImage)

            verifySuspend {
                getAuthUserUseCase()
            }
        }
}

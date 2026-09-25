/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.composeapp

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.v2.runComposeUiTest
import app.cash.turbine.test
import com.iolandarosa.retailhub.composeapp.di.appModules
import com.iolandarosa.retailhub.core.common.maps.MapManager
import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.core.user.domain.interactors.GetAuthUserUseCase
import com.iolandarosa.retailhub.core.user.domain.interactors.GetLocalUserImageUseCase
import com.iolandarosa.retailhub.core.user.domain.model.Address
import com.iolandarosa.retailhub.core.user.domain.model.Coordinates
import com.iolandarosa.retailhub.core.user.domain.model.User
import com.iolandarosa.retailhub.features.home.presentation.HomeViewModel
import com.iolandarosa.retailhub.features.profile.domain.interactors.LoginUseCase
import com.iolandarosa.retailhub.features.profile.presentation.address.AddressViewModel
import com.iolandarosa.retailhub.features.profile.presentation.profile.ProfileViewModel
import dev.mokkery.answering.returns
import dev.mokkery.answering.sequentiallyReturns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import org.koin.compose.KoinIsolatedContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class AppTest {
    private val user =
        User(
            id = 1,
            name = "John Doe",
            image = "image_url",
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
                    street = "address",
                    city = "city",
                    state = "state",
                    stateCode = "stateCode",
                    postalCode = "postalCode",
                    coordinates = Coordinates(lat = 1.0, lng = 1.0),
                    country = "country",
                ),
        )

    private val loginUseCase = mock<LoginUseCase>()
    private val getAuthUserUseCase = mock<GetAuthUserUseCase>()
    private val getLocalUserImageUseCase = mock<GetLocalUserImageUseCase>()
    private val mapManager = mock<MapManager>()

    private lateinit var scheduler: TestCoroutineScheduler
    private lateinit var dispatcher: CoroutineDispatcher
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var addressViewModel: AddressViewModel
    private lateinit var homeViewModel: HomeViewModel

    private val koinApp =
        koinApplication {
            allowOverride(true)
            modules(appModules)
            modules(fakeTestModule)
            modules(
                module {
                    viewModel { profileViewModel }
                    viewModel { addressViewModel }
                    viewModel { homeViewModel }
                },
            )
        }

    @BeforeTest
    fun setup() {
        every { mapManager.getStaticMapUrl(any(), any()) } returns "https://maps.com"

        scheduler = TestCoroutineScheduler()
        dispatcher = StandardTestDispatcher(scheduler)

        profileViewModel =
            ProfileViewModel(
                getAuthUserUseCase = getAuthUserUseCase,
                logoutUseCase = mock(),
                dispatcherProvider = TestDispatcherProvider(dispatcher),
                permissionController = mock(),
                imagePickerController = mock(),
                preferencesManager = mock(),
                getLocalUserImageUseCase = getLocalUserImageUseCase,
                deleteUserImageUseCase = mock(),
                saveUserImageUseCase = mock(),
                deleteUserUseCase = mock(),
                loginUseCase = loginUseCase,
            )

        addressViewModel =
            AddressViewModel(
                dispatcherProvider = TestDispatcherProvider(dispatcher),
                clipboardManager = mock(),
                address = user.address,
                mapManager = mapManager,
            )

        homeViewModel =
            HomeViewModel(
                dispatcherProvider = TestDispatcherProvider(dispatcher),
                getAuthUserUseCase = getAuthUserUseCase,
                getLocalUserImageUseCase = getLocalUserImageUseCase,
            )
    }

    @Test
    fun initialState_renderScreen_showsHomeScreen() =
        runComposeUiTest(runTestContext = dispatcher) {
            everySuspend { getAuthUserUseCase() } returns NetworkResult.Failure.Unauthorized

            setContent {
                KoinIsolatedContext(koinApp) {
                    App()
                }
            }

            scheduler.advanceUntilIdle()

            onNodeWithText("Retail Hub").assertIsDisplayed()
            onNodeWithContentDescription("Go to profile")
                .assertIsDisplayed()
                .assertIsEnabled()
        }

    @Test
    fun onProfileLoaded_clickNavigateProfile_showsProfileScreen() =
        runComposeUiTest(runTestContext = dispatcher) {
            everySuspend { getAuthUserUseCase() } returns NetworkResult.Success(user)
            everySuspend { getLocalUserImageUseCase(any()) } returns null

            setContent {
                KoinIsolatedContext(koinApp) {
                    App()
                }
            }

            onNodeWithContentDescription("Go to profile")
                .performClick()

            homeViewModel.effects.test {
                awaitIdle()
            }

            onNodeWithText(user.name).assertIsDisplayed()
        }

    @Test
    fun errorUnauthorized_renderScreen_showsLoginScreen() =
        runComposeUiTest(runTestContext = dispatcher) {
            everySuspend { getAuthUserUseCase() } returns NetworkResult.Failure.Unauthorized

            setContent {
                KoinIsolatedContext(koinApp) {
                    App()
                }
            }

            onNodeWithContentDescription("Go to profile")
                .performClick()

            homeViewModel.effects.test {
                awaitIdle()
            }

            scheduler.advanceUntilIdle()

            onNodeWithText("Sign in")
                .assertIsDisplayed()
                .assertIsEnabled()
        }

    @Test
    fun signInSuccess_renderScreen_navigatesProfileScreen() =
        runComposeUiTest(runTestContext = dispatcher) {
            everySuspend { getAuthUserUseCase() } sequentiallyReturns
                listOf(
                    NetworkResult.Failure.Unauthorized,
                    NetworkResult.Failure.Unauthorized,
                    NetworkResult.Success(user),
                )
            everySuspend { loginUseCase(any(), any()) } returns
                NetworkResult.Success(Unit)
            everySuspend { getLocalUserImageUseCase(any()) } returns null

            setContent {
                KoinIsolatedContext(koinApp) {
                    App()
                }
            }

            onNodeWithContentDescription("Go to profile")
                .performClick()

            homeViewModel.effects.test {
                awaitIdle()
            }

            onNodeWithText("Sign in")
                .assertIsDisplayed()
                .assertIsEnabled()

            onNodeWithText("Username").performTextInput("username")
            onNodeWithText("Password").performTextInput("password")

            onNodeWithText("Sign in")
                .assertIsDisplayed()
                .performClick()

            scheduler.advanceUntilIdle()

            onNodeWithText(user.name).assertIsDisplayed()
        }

    @Test
    fun componentLoaded_onAddressClick_showsAddressScreen() =
        runComposeUiTest(runTestContext = dispatcher) {
            everySuspend { getAuthUserUseCase() } returns NetworkResult.Success(user)
            everySuspend { getLocalUserImageUseCase(any()) } returns null

            setContent {
                KoinIsolatedContext(koinApp) {
                    App()
                }
            }

            onNodeWithContentDescription("Go to profile")
                .performClick()

            homeViewModel.effects.test {
                awaitIdle()
            }

            scheduler.advanceUntilIdle()

            onNodeWithContentDescription("Address details")
                .performScrollTo()
                .performClick()

            profileViewModel.effects.test {
                awaitIdle()
            }

            onNodeWithText("Address details").assertIsDisplayed()
        }
}

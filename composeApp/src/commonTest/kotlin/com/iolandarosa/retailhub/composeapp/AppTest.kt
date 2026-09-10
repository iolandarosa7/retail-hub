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
import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.features.auth.domain.interactors.GetAuthUserUseCase
import com.iolandarosa.retailhub.features.auth.domain.interactors.LoginUseCase
import com.iolandarosa.retailhub.features.auth.domain.model.Address
import com.iolandarosa.retailhub.features.auth.domain.model.Coordinates
import com.iolandarosa.retailhub.features.auth.domain.model.User
import com.iolandarosa.retailhub.features.auth.presentation.login.LoginViewModel
import com.iolandarosa.retailhub.features.auth.presentation.profile.ProfileViewModel
import dev.mokkery.answering.returns
import dev.mokkery.answering.sequentiallyReturns
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

    private lateinit var scheduler: TestCoroutineScheduler
    private lateinit var dispatcher: CoroutineDispatcher
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var profileViewModel: ProfileViewModel

    private val koinApp =
        koinApplication {
            allowOverride(true)
            modules(appModules)
            modules(fakeTestModule)
            modules(
                module {
                    viewModel { loginViewModel }
                    viewModel { profileViewModel }
                },
            )
        }

    @BeforeTest
    fun setup() {
        scheduler = TestCoroutineScheduler()
        dispatcher = StandardTestDispatcher(scheduler)

        loginViewModel =
            LoginViewModel(
                loginUseCase = loginUseCase,
                dispatcherProvider = TestDispatcherProvider(dispatcher),
            )

        profileViewModel =
            ProfileViewModel(
                getAuthUserUseCase = getAuthUserUseCase,
                logoutUseCase = mock(),
                dispatcherProvider = TestDispatcherProvider(dispatcher),
            )
    }

    @Test
    fun initialStateSuccess_renderScreen_showsProfileScreen() =
        runComposeUiTest(runTestContext = dispatcher) {
            everySuspend { getAuthUserUseCase() } returns NetworkResult.Success(user)

            setContent {
                KoinIsolatedContext(koinApp) {
                    App()
                }
            }

            scheduler.advanceUntilIdle()

            onNodeWithText(user.name)
                .assertIsDisplayed()
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
                    NetworkResult.Success(user),
                )
            everySuspend { loginUseCase(any(), any()) } returns
                NetworkResult.Success(Unit)

            setContent {
                KoinIsolatedContext(koinApp) {
                    App()
                }
            }

            profileViewModel.effects.test {
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

            loginViewModel.effects.test {
                awaitIdle()
            }

            onNodeWithText(user.name).assertIsDisplayed()
        }

    @Test
    fun componentLoaded_onAddressClick_showsAddressScreen() =
        runComposeUiTest(runTestContext = dispatcher) {
            everySuspend { getAuthUserUseCase() } returns NetworkResult.Success(user)

            setContent {
                KoinIsolatedContext(koinApp) {
                    App()
                }
            }

            scheduler.advanceUntilIdle()

            onNodeWithContentDescription("Address details")
                .performScrollTo()
                .performClick()

            profileViewModel.effects.test {
                awaitIdle()
            }

            onNodeWithText("MAP").assertIsDisplayed()
        }
}

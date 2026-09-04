/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.composeapp

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.v2.runComposeUiTest
import com.iolandarosa.retailhub.composeapp.di.appModules
import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.features.auth.domain.interactors.LoginUseCase
import com.iolandarosa.retailhub.features.auth.presentation.login.LoginViewModel
import dev.mokkery.answering.returns
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
    private val loginUseCase = mock<LoginUseCase>()

    private lateinit var scheduler: TestCoroutineScheduler
    private lateinit var dispatcher: CoroutineDispatcher
    private lateinit var loginViewModel: LoginViewModel

    private val koinApp =
        koinApplication {
            allowOverride(true)
            modules(appModules)
            modules(fakeTestModule)
            modules(
                module {
                    viewModel { loginViewModel }
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
    }

    @Test
    fun initialState_renderScreen_showsLoginScreen() =
        runComposeUiTest {
            setContent {
                KoinIsolatedContext(koinApp) {
                    App()
                }
            }

            onNodeWithText("Sign in")
                .assertIsDisplayed()
                .assertIsEnabled()
        }

    @Test
    fun signInSuccess_renderScreen_navigatesProfileScreen() =
        runComposeUiTest(runTestContext = dispatcher) {
            everySuspend { loginUseCase(any(), any()) } returns
                NetworkResult.Success(Unit)

            setContent {
                KoinIsolatedContext(koinApp) {
                    App()
                }
            }

            onNodeWithText("Username").performTextInput("username")
            onNodeWithText("Password").performTextInput("password")

            onNodeWithText("Sign in")
                .assertIsDisplayed()
                .performClick()

            scheduler.advanceUntilIdle()

            onNodeWithText("Sign in")
                .assertIsNotDisplayed()

            onNodeWithText("Back")
                .assertIsDisplayed()
                .performClick()

            onNodeWithText("Sign in")
                .assertIsDisplayed()
        }
}

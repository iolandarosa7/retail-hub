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
import com.iolandarosa.retailhub.core.model.ApiErrorResponse
import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.features.auth.TestDispatcherProvider
import com.iolandarosa.retailhub.features.auth.domain.interactors.GetAuthUserUseCase
import com.iolandarosa.retailhub.features.auth.domain.model.User
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlin.test.BeforeTest
import kotlin.test.Test

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
    fun profileScreenDisplaysLoadingAndThenSuccess() =
        runComposeUiTest(runTestContext = dispatcher) {
            val user =
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
                    address = "123 Main St",
                )

            everySuspend { getAuthUserUseCase() } returns NetworkResult.Success(user)

            setContent {
                ProfileScreen(
                    paddingValues = PaddingValues(),
                    viewModel = viewModel,
                )
            }

            // Initially it might be loading, but runComposeUiTest handles synchronization.
            // If it's too fast, it might already be in success state.

            scheduler.advanceUntilIdle()

            onNodeWithText("John Doe").assertIsDisplayed()
            onNodeWithText("admin").assertIsDisplayed()
            onNodeWithText("john@example.com").assertIsDisplayed()
        }

    @Test
    fun profileScreenDisplaysErrorState() =
        runComposeUiTest(runTestContext = dispatcher) {
            val errorMessage = "Error message"
            everySuspend { getAuthUserUseCase() } returns NetworkResult.Failure.ApiError(ApiErrorResponse(errorMessage))

            setContent {
                ProfileScreen(
                    paddingValues = PaddingValues(),
                    viewModel = viewModel,
                )
            }

            scheduler.advanceUntilIdle()

            onNodeWithText(errorMessage).assertIsDisplayed()
        }
}

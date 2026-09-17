/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.presentation.profile

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.v2.runComposeUiTest
import app.cash.turbine.test
import com.iolandarosa.retailhub.core.model.ApiErrorResponse
import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.core.storage.domain.model.ImageStorageResult
import com.iolandarosa.retailhub.core.ui.images.ImagePickerController
import com.iolandarosa.retailhub.core.ui.permissions.AppPermission
import com.iolandarosa.retailhub.core.ui.permissions.AppPermissionStatus
import com.iolandarosa.retailhub.core.ui.permissions.PermissionController
import com.iolandarosa.retailhub.core.ui.snackbar.SnackBarData
import com.iolandarosa.retailhub.features.profile.TestDispatcherProvider
import com.iolandarosa.retailhub.features.profile.domain.interactors.DeleteUserImageUseCase
import com.iolandarosa.retailhub.features.profile.domain.interactors.DeleteUserUseCase
import com.iolandarosa.retailhub.features.profile.domain.interactors.GetAuthUserUseCase
import com.iolandarosa.retailhub.features.profile.domain.interactors.GetLocalUserImageUseCase
import com.iolandarosa.retailhub.features.profile.domain.interactors.LogoutUseCase
import com.iolandarosa.retailhub.features.profile.domain.interactors.SaveUserImageUseCase
import com.iolandarosa.retailhub.features.profile.domain.model.Address
import com.iolandarosa.retailhub.features.profile.utils.TestUser
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
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class ProfileScreenTest {
    private val getAuthUserUseCase = mock<GetAuthUserUseCase>()
    private val logoutUseCase = mock<LogoutUseCase>()
    private val permissionController = mock<PermissionController>()
    private val imagePickerController = mock<ImagePickerController>()
    private val getLocalUserImageUseCase = mock<GetLocalUserImageUseCase>()
    private val saveUserImageUseCase = mock<SaveUserImageUseCase>()
    private val deleteUserImageUseCase = mock<DeleteUserImageUseCase>()

    private val deleteUserUseCase = mock<DeleteUserUseCase>()

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
                logoutUseCase = logoutUseCase,
                dispatcherProvider = TestDispatcherProvider(dispatcher),
                permissionController = permissionController,
                imagePickerController = imagePickerController,
                preferencesManager = mock(),
                getLocalUserImageUseCase = getLocalUserImageUseCase,
                saveUserImageUseCase = saveUserImageUseCase,
                deleteUserImageUseCase = deleteUserImageUseCase,
                deleteUserUseCase = deleteUserUseCase,
            )
    }

    @Composable
    private fun TestProfileScreen(
        navigateToLogin: () -> Unit = {},
        navigateToAddressDetails: (Address) -> Unit = {},
        showSnackBar: (SnackBarData) -> Unit = {},
    ) = ProfileScreen(
        paddingValues = PaddingValues(),
        navigateToLogin = navigateToLogin,
        viewModel = viewModel,
        navigateToAddressDetails = navigateToAddressDetails,
        showSnackBar = showSnackBar,
    )

    @Test
    fun success_screenLoaded_displayUserData() =
        runComposeUiTest(runTestContext = dispatcher) {
            val user = TestUser.user

            everySuspend { getAuthUserUseCase() } returns NetworkResult.Success(user)
            everySuspend { getLocalUserImageUseCase(user.id) } returns null

            setContent { TestProfileScreen() }

            onNodeWithContentDescription("Loading User Profile").assertIsDisplayed()
            onNodeWithContentDescription("Address details").assertIsNotEnabled()

            scheduler.advanceUntilIdle()

            onNodeWithContentDescription("Update profile picture")
                .assertIsDisplayed()
                .assertIsEnabled()
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
                TestProfileScreen(navigateToLogin = { callbackCalled = true })
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

            setContent { TestProfileScreen() }

            scheduler.advanceUntilIdle()

            onNodeWithText(errorMessage).assertIsDisplayed()
        }

    @Test
    fun logoutClick_screenLoaded_expectCallbackCalled() =
        runComposeUiTest(runTestContext = dispatcher) {
            val user = TestUser.user
            var callbackCalled = false

            everySuspend { getAuthUserUseCase() } returns NetworkResult.Success(user)
            everySuspend { getLocalUserImageUseCase(user.id) } returns null
            everySuspend { logoutUseCase() } returns Unit

            setContent {
                TestProfileScreen(navigateToLogin = { callbackCalled = true })
            }

            scheduler.advanceUntilIdle()

            onNodeWithText("Logout")
                .performScrollTo()
                .assertIsDisplayed()
                .assertIsEnabled()
                .performClick()

            viewModel.effects.test {
                awaitIdle()
                assertTrue(callbackCalled)
            }
        }

    @Test
    fun success_addressClick_expectedCallbackCalled() =
        runComposeUiTest(runTestContext = dispatcher) {
            val user = TestUser.user
            var address: Address? = null

            everySuspend { getAuthUserUseCase() } returns NetworkResult.Success(user)
            everySuspend { getLocalUserImageUseCase(user.id) } returns null

            setContent { TestProfileScreen(navigateToAddressDetails = { address = it }) }

            scheduler.advanceUntilIdle()

            onNodeWithContentDescription("Address details")
                .performScrollTo()
                .assertIsEnabled()
                .performClick()

            viewModel.effects.test {
                awaitIdle()
                assertEquals(user.address, address)
            }
        }

    @Test
    fun success_showImagePickerClick_expectedPickerBottomSheetShown() =
        runComposeUiTest(runTestContext = dispatcher) {
            val user = TestUser.user
            everySuspend { getAuthUserUseCase() } returns NetworkResult.Success(user)
            everySuspend { getLocalUserImageUseCase(user.id) } returns null

            setContent { TestProfileScreen() }

            scheduler.advanceUntilIdle()

            onNodeWithContentDescription("Update profile picture")
                .performClick()

            onNodeWithText("Camera").assertIsDisplayed()
        }

    @Test
    fun success_cameraRationale_expectedPermissionDialogShown() =
        runComposeUiTest(runTestContext = dispatcher) {
            val user = TestUser.user
            everySuspend { getAuthUserUseCase() } returns NetworkResult.Success(user)
            everySuspend { getLocalUserImageUseCase(user.id) } returns null
            everySuspend { permissionController.checkPermission(any()) } returns
                AppPermissionStatus.ShouldRequest(showRational = true)

            setContent { TestProfileScreen() }

            scheduler.advanceUntilIdle()

            viewModel.onIntent(
                ProfileContract.Intent.CheckImagePermissions(AppPermission.Camera, user.id),
            )

            scheduler.advanceUntilIdle()

            onNodeWithText("Allow camera").assertIsDisplayed()
            onNodeWithText(
                "Camera access is needed to take photos in the app. Please allow camera access to continue",
            ).assertIsDisplayed()
            onNodeWithText("Allow camera access").assertIsDisplayed()
        }

    @Test
    fun success_cameraOpenSettings_expectedPermissionDialogShown() =
        runComposeUiTest(runTestContext = dispatcher) {
            val user = TestUser.user
            everySuspend { getAuthUserUseCase() } returns NetworkResult.Success(user)
            everySuspend { getLocalUserImageUseCase(user.id) } returns null
            everySuspend { permissionController.checkPermission(any()) } returns AppPermissionStatus.Denied

            setContent { TestProfileScreen() }

            scheduler.advanceUntilIdle()

            viewModel.onIntent(
                ProfileContract.Intent.CheckImagePermissions(AppPermission.Camera, user.id),
            )

            scheduler.advanceUntilIdle()

            onNodeWithText("Camera access required").assertIsDisplayed()
            onNodeWithText(
                "Camera access has been denied. Please enable camera access in Settings to use this feature",
            ).assertIsDisplayed()
            onNodeWithText("Open settings").assertIsDisplayed()
        }

    @Test
    fun success_galleryOpenSettings_expectedPermissionDialogShown() =
        runComposeUiTest(runTestContext = dispatcher) {
            val user = TestUser.user
            everySuspend { getAuthUserUseCase() } returns NetworkResult.Success(user)
            everySuspend { getLocalUserImageUseCase(user.id) } returns null
            everySuspend { permissionController.checkPermission(any()) } returns AppPermissionStatus.Denied

            setContent { TestProfileScreen() }

            scheduler.advanceUntilIdle()

            viewModel.onIntent(
                ProfileContract.Intent.CheckImagePermissions(AppPermission.Gallery, user.id),
            )

            scheduler.advanceUntilIdle()

            onNodeWithText("Photo library access required").assertIsDisplayed()
            onNodeWithText(
                "Photo library access has been denied. " +
                    "Please enable photo library access in Settings to use this feature",
            ).assertIsDisplayed()
            onNodeWithText("Open settings").assertIsDisplayed()
        }

    @Test
    fun errorDeleteImage_deleteImageClick_expectedCallbackCalled() =
        runComposeUiTest(runTestContext = dispatcher) {
            val user = TestUser.user
            val expectedError = "error"
            var snackBarData: SnackBarData? = null

            everySuspend { getAuthUserUseCase() } returns NetworkResult.Success(user)
            everySuspend { getLocalUserImageUseCase(user.id) } returns byteArrayOf(1)
            everySuspend { deleteUserImageUseCase(user.id) } returns ImageStorageResult.Failure.General(expectedError)

            setContent { TestProfileScreen(showSnackBar = { snackBarData = it }) }

            scheduler.advanceUntilIdle()

            onNodeWithContentDescription("Delete profile picture")
                .performScrollTo()
                .assertIsEnabled()
                .performClick()

            viewModel.effects.test {
                awaitIdle()
                assertEquals(expectedError, snackBarData?.message)
            }
        }

    @Test
    fun success_deleteAccountConfirmClick_expectCallbackCalled() =
        runComposeUiTest(runTestContext = dispatcher) {
            val user = TestUser.user
            var callbackCalled = false

            everySuspend { getAuthUserUseCase() } returns NetworkResult.Success(user)
            everySuspend { getLocalUserImageUseCase(user.id) } returns null
            everySuspend { deleteUserUseCase(any()) } returns true

            setContent {
                TestProfileScreen(navigateToLogin = { callbackCalled = true })
            }

            scheduler.advanceUntilIdle()

            onNodeWithText("Delete account")
                .performScrollTo()
                .assertIsDisplayed()
                .assertIsEnabled()
                .performClick()

            onNodeWithText("Delete your account?").assertIsDisplayed()

            // Click confirm in dialog
            onAllNodesWithText("Delete account").onLast().performClick()

            viewModel.effects.test {
                awaitIdle()
                assertTrue(callbackCalled)
            }
        }

    @Test
    fun failure_deleteAccountConfirmClick_expectSnackBarCalled() =
        runComposeUiTest(runTestContext = dispatcher) {
            val user = TestUser.user
            var callbackCalled = false

            everySuspend { getAuthUserUseCase() } returns NetworkResult.Success(user)
            everySuspend { getLocalUserImageUseCase(user.id) } returns null
            everySuspend { deleteUserUseCase(any()) } returns false

            setContent {
                TestProfileScreen(showSnackBar = { callbackCalled = true })
            }

            scheduler.advanceUntilIdle()

            onNodeWithText("Delete account")
                .performScrollTo()
                .assertIsDisplayed()
                .assertIsEnabled()
                .performClick()

            onNodeWithText("Delete your account?").assertIsDisplayed()

            // Click confirm in dialog
            onAllNodesWithText("Delete account").onLast().performClick()

            viewModel.effects.test {
                awaitIdle()
                assertTrue(callbackCalled)
            }
        }

    @Test
    fun cancel_deleteAccountConfirmation_expectDialogDismissed() =
        runComposeUiTest(runTestContext = dispatcher) {
            val user = TestUser.user

            everySuspend { getAuthUserUseCase() } returns NetworkResult.Success(user)
            everySuspend { getLocalUserImageUseCase(user.id) } returns null

            setContent { TestProfileScreen() }

            scheduler.advanceUntilIdle()

            onNodeWithText("Delete account")
                .performScrollTo()
                .performClick()

            onNodeWithText("Delete your account?").assertIsDisplayed()

            onNodeWithText("Cancel").performClick()

            onNodeWithText("Delete your account?").assertDoesNotExist()
        }
}

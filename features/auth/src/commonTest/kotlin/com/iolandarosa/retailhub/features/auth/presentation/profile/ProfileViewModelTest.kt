/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.presentation.profile

import app.cash.turbine.test
import com.iolandarosa.retailhub.core.datastore.domain.PreferencesManager
import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.core.model.PreferencesKey
import com.iolandarosa.retailhub.core.ui.extension.toImageSource
import com.iolandarosa.retailhub.core.ui.extension.toOpenSettingsDialog
import com.iolandarosa.retailhub.core.ui.images.ImagePickerController
import com.iolandarosa.retailhub.core.ui.permissions.AppPermission
import com.iolandarosa.retailhub.core.ui.permissions.AppPermissionStatus
import com.iolandarosa.retailhub.core.ui.permissions.PermissionController
import com.iolandarosa.retailhub.core.ui.permissions.PermissionDialogActionType
import com.iolandarosa.retailhub.features.auth.TestDispatcherProvider
import com.iolandarosa.retailhub.features.auth.domain.interactors.GetAuthUserUseCase
import com.iolandarosa.retailhub.features.auth.domain.interactors.LogoutUseCase
import com.iolandarosa.retailhub.features.auth.presentation.profile.ProfileContract.Effect
import com.iolandarosa.retailhub.features.auth.presentation.profile.ProfileContract.Intent
import com.iolandarosa.retailhub.features.auth.presentation.profile.ProfileContract.LogoutRequestState
import com.iolandarosa.retailhub.features.auth.presentation.profile.ProfileContract.UserRequestState
import com.iolandarosa.retailhub.features.auth.utils.TestUser
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
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
class ProfileViewModelTest {
    private val getAuthUserUseCase = mock<GetAuthUserUseCase>()
    private val logoutUseCase = mock<LogoutUseCase>()
    private val permissionController = mock<PermissionController>()
    private val preferencesManager = mock<PreferencesManager>()
    private val imagePickerController = mock<ImagePickerController>()
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
                permissionController,
                imagePickerController,
                preferencesManager,
            )
    }

    @Test
    fun initialInstance_hasExpectedState() {
        assertEquals(UserRequestState.Initial, viewModel.state.value.userRequest)
        assertEquals(LogoutRequestState.Initial, viewModel.state.value.logoutRequest)
        assertFalse(viewModel.state.value.isRefreshing)
        assertFalse(viewModel.state.value.showImagePicker)
        assertNull(viewModel.state.value.permissionDialog)
        assertNull(viewModel.state.value.imageBytes)
        assertFalse(viewModel.state.value.showPermissionsDialog)
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

    @Test
    fun toggleShowImagePicker_showImagePickerBottomSheet_hasExpectedState() {
        viewModel.onIntent(Intent.ShowImagePickerBottomSheet(true))

        assertTrue(viewModel.state.value.showImagePicker)

        viewModel.onIntent(Intent.ShowImagePickerBottomSheet(false))

        assertFalse(viewModel.state.value.showImagePicker)
    }

    @Test
    fun showImagePickerTrue_checkImagePermissions_hasExpectedState() {
        viewModel.onIntent(Intent.ShowImagePickerBottomSheet(true))

        assertTrue(viewModel.state.value.showImagePicker)

        viewModel.onIntent(Intent.CheckImagePermissions(permission = AppPermission.Camera))

        assertFalse(viewModel.state.value.showImagePicker)
    }

    @Test
    fun closePermissionsDialog_setsPermissionDialogToNull() {
        viewModel.onIntent(Intent.ClosePermissionsDialog)
        assertNull(viewModel.state.value.permissionDialog)
    }

    @Test
    fun checkImagePermissions_denied_setsPermissionDialog() =
        runTest(scheduler) {
            everySuspend { permissionController.checkPermission(AppPermission.Camera) } returns AppPermissionStatus.Denied

            viewModel.onIntent(Intent.CheckImagePermissions(AppPermission.Camera))
            advanceUntilIdle()

            assertEquals(AppPermission.Camera.toOpenSettingsDialog(), viewModel.state.value.permissionDialog)
            assertFalse(viewModel.state.value.showImagePicker)
        }

    @Test
    fun checkImagePermissions_granted_capturesImage() =
        runTest(scheduler) {
            val imageBytes = byteArrayOf(1, 2, 3)
            everySuspend { permissionController.checkPermission(AppPermission.Camera) } returns AppPermissionStatus.Granted
            everySuspend { imagePickerController.pickImage(any()) } returns imageBytes

            viewModel.onIntent(Intent.CheckImagePermissions(AppPermission.Camera))
            advanceUntilIdle()

            assertTrue(imageBytes.contentEquals(viewModel.state.value.imageBytes))
            assertFalse(viewModel.state.value.showImagePicker)
            verifySuspend { imagePickerController.pickImage(AppPermission.Camera.toImageSource()) }
        }

    @Test
    fun checkImagePermissions_shouldRequestRational_setsPermissionDialog() =
        runTest(scheduler) {
            everySuspend { permissionController.checkPermission(AppPermission.Camera) } returns
                AppPermissionStatus.ShouldRequest(showRational = true)

            viewModel.onIntent(Intent.CheckImagePermissions(AppPermission.Camera))
            advanceUntilIdle()

            val dialog = viewModel.state.value.permissionDialog
            assertNotNull(dialog)
            assertEquals(PermissionDialogActionType.CameraRational, dialog.type)
            assertFalse(viewModel.state.value.showImagePicker)
        }

    @Test
    fun checkImagePermissions_shouldRequestNoRational_requestsPermission() =
        runTest(scheduler) {
            everySuspend { permissionController.checkPermission(AppPermission.Camera) } returns
                AppPermissionStatus.ShouldRequest(showRational = false)
            everySuspend { permissionController.requestPermission(AppPermission.Camera) } returns AppPermissionStatus.Granted
            everySuspend { imagePickerController.pickImage(any()) } returns byteArrayOf(1)

            viewModel.onIntent(Intent.CheckImagePermissions(AppPermission.Camera))
            advanceUntilIdle()

            verifySuspend { permissionController.requestPermission(AppPermission.Camera) }
        }

    @Test
    fun confirmPermissionAction_cameraRational_requestsPermission() =
        runTest(scheduler) {
            everySuspend { preferencesManager.setPermissionRequested(PreferencesKey.CAMERA) } returns Unit
            everySuspend { permissionController.requestPermission(AppPermission.Camera) } returns AppPermissionStatus.Granted
            everySuspend { imagePickerController.pickImage(any()) } returns byteArrayOf(1)

            viewModel.onIntent(Intent.ConfirmPermissionAction(PermissionDialogActionType.CameraRational))
            advanceUntilIdle()

            verifySuspend { preferencesManager.setPermissionRequested(PreferencesKey.CAMERA) }
            verifySuspend { permissionController.requestPermission(AppPermission.Camera) }
            assertNull(viewModel.state.value.permissionDialog)
        }

    @Test
    fun confirmPermissionAction_openSettings_launchesSettings() =
        runTest(scheduler) {
            every { permissionController.launchSettings() } returns Unit

            viewModel.onIntent(Intent.ConfirmPermissionAction(PermissionDialogActionType.OpenSettings))
            advanceUntilIdle()

            verify { permissionController.launchSettings() }
            assertNull(viewModel.state.value.permissionDialog)
        }
}

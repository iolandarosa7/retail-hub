/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iolandarosa.retailhub.core.common.dispatcher.DispatcherProvider
import com.iolandarosa.retailhub.core.datastore.domain.PreferencesManager
import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.core.storage.domain.model.ImageStorageResult
import com.iolandarosa.retailhub.core.ui.extension.toImageSource
import com.iolandarosa.retailhub.core.ui.extension.toOpenSettingsDialog
import com.iolandarosa.retailhub.core.ui.extension.toPreferencesKey
import com.iolandarosa.retailhub.core.ui.extension.toUiError
import com.iolandarosa.retailhub.core.ui.images.ImagePickerController
import com.iolandarosa.retailhub.core.ui.permissions.AppPermission
import com.iolandarosa.retailhub.core.ui.permissions.AppPermissionStatus
import com.iolandarosa.retailhub.core.ui.permissions.PermissionController
import com.iolandarosa.retailhub.core.ui.permissions.PermissionDialog
import com.iolandarosa.retailhub.core.ui.permissions.PermissionDialogActionType
import com.iolandarosa.retailhub.core.ui.snackbar.SnackBarData
import com.iolandarosa.retailhub.core.ui.snackbar.SnackBarType
import com.iolandarosa.retailhub.features.profile.domain.extensions.toSnackBarData
import com.iolandarosa.retailhub.features.profile.domain.interactors.DeleteUserImageUseCase
import com.iolandarosa.retailhub.features.profile.domain.interactors.DeleteUserUseCase
import com.iolandarosa.retailhub.features.profile.domain.interactors.GetAuthUserUseCase
import com.iolandarosa.retailhub.features.profile.domain.interactors.GetLocalUserImageUseCase
import com.iolandarosa.retailhub.features.profile.domain.interactors.LogoutUseCase
import com.iolandarosa.retailhub.features.profile.domain.interactors.SaveUserImageUseCase
import com.iolandarosa.retailhub.features.profile.domain.model.Address
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retailhub.features.profile.generated.resources.Res
import retailhub.features.profile.generated.resources.camera_permission_rational_confirm_btn
import retailhub.features.profile.generated.resources.camera_permission_rational_description
import retailhub.features.profile.generated.resources.camera_permission_rational_title
import retailhub.features.profile.generated.resources.error_user_delete

class ProfileViewModel(
    private val getAuthUserUseCase: GetAuthUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val dispatcherProvider: DispatcherProvider,
    val permissionController: PermissionController,
    val imagePickerController: ImagePickerController,
    private val preferencesManager: PreferencesManager,
    private val getLocalUserImageUseCase: GetLocalUserImageUseCase,
    private val saveUserImageUseCase: SaveUserImageUseCase,
    private val deleteUserImageUseCase: DeleteUserImageUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
) : ViewModel() {
    private val _state: MutableStateFlow<ProfileContract.State> =
        MutableStateFlow(ProfileContract.State())
    val state = _state.asStateFlow()

    private val _effects = Channel<ProfileContract.Effect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onIntent(intent: ProfileContract.Intent) {
        when (intent) {
            ProfileContract.Intent.LoadProfile -> {
                getAuthUser(isRefresh = false)
            }

            ProfileContract.Intent.Logout -> {
                logout()
            }

            ProfileContract.Intent.RefreshProfile -> {
                getAuthUser(isRefresh = true)
            }

            is ProfileContract.Intent.ViewAddressDetails -> {
                viewAddressDetails(intent.address)
            }

            is ProfileContract.Intent.OnImageClick -> {
                if (intent.isDelete) {
                    deleteUserImage(intent.userId)
                } else {
                    _state.update { it.copy(showImagePicker = true) }
                }
            }

            is ProfileContract.Intent.HideImagePickerBottomSheet -> {
                _state.update { it.copy(showImagePicker = false) }
            }

            is ProfileContract.Intent.CheckImagePermissions -> {
                checkImagePermissions(intent.permission, intent.userId)
            }

            ProfileContract.Intent.ClosePermissionsDialog -> {
                _state.update { it.copy(permissionDialog = null) }
            }

            is ProfileContract.Intent.ConfirmPermissionAction -> {
                handleDialogAction(intent.type, intent.userId)
            }

            is ProfileContract.Intent.DeleteUser -> {
                deleteUser(intent.userId)
            }
        }
    }

    private fun getAuthUser(isRefresh: Boolean) {
        if (state.value.userRequest is ProfileContract.UserRequestState.Loading) return
        if (isRefresh && state.value.isRefreshing) return

        _state.update {
            if (isRefresh) {
                it.copy(isRefreshing = true)
            } else {
                it.copy(userRequest = ProfileContract.UserRequestState.Loading)
            }
        }

        viewModelScope.launch(dispatcherProvider.main) {
            when (val result = getAuthUserUseCase()) {
                is NetworkResult.Failure.Unauthorized -> {
                    _state.update {
                        it.copy(
                            userRequest = ProfileContract.UserRequestState.Initial,
                            isRefreshing = false,
                        )
                    }

                    _effects.send(ProfileContract.Effect.NavigateToLogin)
                }

                is NetworkResult.Failure -> {
                    _state.update {
                        it.copy(
                            userRequest = ProfileContract.UserRequestState.Error(result.toUiError()),
                            isRefreshing = false,
                        )
                    }
                }

                is NetworkResult.Success -> {
                    val localUserImage = getLocalUserImageUseCase(result.data.id)

                    _state.update {
                        it.copy(
                            userRequest = ProfileContract.UserRequestState.Success(result.data),
                            imageBytes = localUserImage,
                            isRefreshing = false,
                        )
                    }
                }
            }
        }
    }

    private fun logout() {
        if (state.value.logoutRequest is ProfileContract.LogoutRequestState.Loading) return

        _state.update { it.copy(logoutRequest = ProfileContract.LogoutRequestState.Loading) }

        viewModelScope.launch(dispatcherProvider.main) {
            logoutUseCase()
            _state.update { it.copy(logoutRequest = ProfileContract.LogoutRequestState.Initial) }
            _effects.send(ProfileContract.Effect.NavigateToLogin)
        }
    }

    private fun viewAddressDetails(address: Address) {
        viewModelScope.launch(dispatcherProvider.main) {
            _effects.send(ProfileContract.Effect.NavigateToAddressDetails(address))
        }
    }

    private fun checkImagePermissions(
        permission: AppPermission,
        userId: Int,
    ) {
        _state.update { it.copy(showImagePicker = false) }

        viewModelScope.launch(dispatcherProvider.main) {
            when (val status = permissionController.checkPermission(permission)) {
                AppPermissionStatus.Denied -> {
                    _state.update {
                        it.copy(
                            permissionDialog = permission.toOpenSettingsDialog(),
                        )
                    }
                }

                AppPermissionStatus.Granted -> {
                    captureImage(permission, userId)
                }

                is AppPermissionStatus.ShouldRequest -> {
                    if (status.showRational) {
                        _state.update {
                            it.copy(
                                permissionDialog =
                                    PermissionDialog(
                                        titleId = Res.string.camera_permission_rational_title,
                                        descriptionId = Res.string.camera_permission_rational_description,
                                        confirmButtonLabelId = Res.string.camera_permission_rational_confirm_btn,
                                        type = PermissionDialogActionType.CameraRational,
                                    ),
                            )
                        }
                    } else {
                        requestPermissions(permission, userId)
                    }
                }
            }
        }
    }

    private suspend fun requestPermissions(
        permission: AppPermission,
        userId: Int,
    ) {
        val result = permissionController.requestPermission(permission)
        if (result == AppPermissionStatus.Granted) {
            captureImage(permission, userId)
        }
    }

    private fun handleDialogAction(
        permissionDialogActionType: PermissionDialogActionType,
        userId: Int,
    ) {
        _state.update { it.copy(permissionDialog = null) }

        when (permissionDialogActionType) {
            PermissionDialogActionType.CameraRational -> {
                viewModelScope.launch(dispatcherProvider.main) {
                    preferencesManager.setPermissionRequested(AppPermission.Camera.toPreferencesKey())
                    requestPermissions(AppPermission.Camera, userId)
                }
            }

            PermissionDialogActionType.OpenSettings -> {
                permissionController.launchSettings()
            }
        }
    }

    private suspend fun captureImage(
        permission: AppPermission,
        userId: Int,
    ) {
        val imageBytes = imagePickerController.pickImage(permission.toImageSource())

        if (imageBytes != null) {
            when (val result = saveUserImageUseCase(userId, imageBytes)) {
                is ImageStorageResult.Failure -> {
                    _effects.send(
                        ProfileContract.Effect.ShowSnackBarError(
                            result.toSnackBarData(false),
                        ),
                    )
                }

                ImageStorageResult.Success -> {
                    _state.update { it.copy(imageBytes = imageBytes) }
                }
            }
        }
    }

    private fun deleteUserImage(userId: Int) {
        viewModelScope.launch(dispatcherProvider.main) {
            when (val result = deleteUserImageUseCase(userId)) {
                is ImageStorageResult.Failure -> {
                    _effects.send(
                        ProfileContract.Effect.ShowSnackBarError(
                            result.toSnackBarData(true),
                        ),
                    )
                }

                ImageStorageResult.Success -> {
                    _state.update { it.copy(imageBytes = null) }
                }
            }
        }
    }

    private fun deleteUser(userId: Int) {
        if (state.value.deleteUserRequest is ProfileContract.DeleteUserRequestState.Loading) return

        _state.update { it.copy(deleteUserRequest = ProfileContract.DeleteUserRequestState.Loading) }

        viewModelScope.launch(dispatcherProvider.main) {
            val isDeleted = deleteUserUseCase(userId)

            _state.update { it.copy(deleteUserRequest = ProfileContract.DeleteUserRequestState.Initial) }

            _effects.send(
                if (isDeleted) {
                    ProfileContract.Effect.NavigateToLogin
                } else {
                    ProfileContract.Effect.ShowSnackBarError(
                        SnackBarData(
                            messageId = Res.string.error_user_delete,
                            type = SnackBarType.ERROR,
                        ),
                    )
                },
            )
        }
    }
}

/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iolandarosa.retailhub.core.common.dispatcher.DispatcherProvider
import com.iolandarosa.retailhub.core.datastore.domain.PreferencesManager
import com.iolandarosa.retailhub.core.model.NetworkResult
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
import com.iolandarosa.retailhub.features.auth.domain.interactors.GetAuthUserUseCase
import com.iolandarosa.retailhub.features.auth.domain.interactors.LogoutUseCase
import com.iolandarosa.retailhub.features.auth.domain.model.Address
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retailhub.features.auth.generated.resources.Res
import retailhub.features.auth.generated.resources.camera_permission_rational_confirm_btn
import retailhub.features.auth.generated.resources.camera_permission_rational_description
import retailhub.features.auth.generated.resources.camera_permission_rational_title

class ProfileViewModel(
    private val getAuthUserUseCase: GetAuthUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val dispatcherProvider: DispatcherProvider,
    val permissionController: PermissionController,
    val imagePickerController: ImagePickerController,
    private val preferencesManager: PreferencesManager,
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

            is ProfileContract.Intent.ShowImagePickerBottomSheet -> {
                _state.update { it.copy(showImagePicker = intent.show) }
            }

            is ProfileContract.Intent.CheckImagePermissions -> {
                checkImagePermissions(intent.permission)
            }

            ProfileContract.Intent.ClosePermissionsDialog -> {
                _state.update { it.copy(permissionDialog = null) }
            }

            is ProfileContract.Intent.ConfirmPermissionAction -> {
                handleDialogAction(intent.type)
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
                    _state.update {
                        it.copy(
                            userRequest = ProfileContract.UserRequestState.Success(result.data),
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

    private fun checkImagePermissions(permission: AppPermission) {
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
                    captureImage(permission)
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
                        requestPermissions(permission)
                    }
                }
            }
        }
    }

    private suspend fun requestPermissions(permission: AppPermission) {
        val result = permissionController.requestPermission(permission)
        if (result == AppPermissionStatus.Granted) {
            captureImage(permission)
        }
    }

    private fun handleDialogAction(permissionDialogActionType: PermissionDialogActionType) {
        _state.update { it.copy(permissionDialog = null) }

        when (permissionDialogActionType) {
            PermissionDialogActionType.CameraRational -> {
                viewModelScope.launch(dispatcherProvider.main) {
                    preferencesManager.setPermissionRequested(AppPermission.Camera.toPreferencesKey())
                    requestPermissions(AppPermission.Camera)
                }
            }

            PermissionDialogActionType.OpenSettings -> {
                permissionController.launchSettings()
            }
        }
    }

    private suspend fun captureImage(permission: AppPermission) {
        val imageBytes = imagePickerController.pickImage(permission.toImageSource())

        if (imageBytes != null) {
            _state.update { it.copy(imageBytes = imageBytes) }
        }
    }
}

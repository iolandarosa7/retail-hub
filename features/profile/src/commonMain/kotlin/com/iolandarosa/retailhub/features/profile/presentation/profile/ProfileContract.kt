/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.presentation.profile

import com.iolandarosa.retailhub.core.ui.error.UiError
import com.iolandarosa.retailhub.core.ui.permissions.AppPermission
import com.iolandarosa.retailhub.core.ui.permissions.PermissionDialog
import com.iolandarosa.retailhub.core.ui.permissions.PermissionDialogActionType
import com.iolandarosa.retailhub.core.ui.snackbar.SnackBarData
import com.iolandarosa.retailhub.features.profile.domain.model.Address
import com.iolandarosa.retailhub.features.profile.domain.model.User

interface ProfileContract {
    data class State(
        val userRequest: UserRequestState = UserRequestState.Initial,
        val logoutRequest: LogoutRequestState = LogoutRequestState.Initial,
        val deleteUserRequest: DeleteUserRequestState = DeleteUserRequestState.Initial,
        val isRefreshing: Boolean = false,
        val showImagePicker: Boolean = false,
        val permissionDialog: PermissionDialog? = null,
        val imageBytes: ByteArray? = null,
    ) {
        val isInteractionEnabled: Boolean
            get() =
                userRequest !is UserRequestState.Loading &&
                    !isRefreshing &&
                    logoutRequest !is LogoutRequestState.Loading &&
                    deleteUserRequest !is DeleteUserRequestState.Loading

        val showLogoutLoading: Boolean
            get() = logoutRequest is LogoutRequestState.Loading

        val showDeleteLoading: Boolean
            get() = deleteUserRequest is DeleteUserRequestState.Loading

        val showPermissionsDialog: Boolean
            get() = permissionDialog != null

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as State

            if (isRefreshing != other.isRefreshing) return false
            if (showImagePicker != other.showImagePicker) return false
            if (userRequest != other.userRequest) return false
            if (logoutRequest != other.logoutRequest) return false
            if (permissionDialog != other.permissionDialog) return false
            if (deleteUserRequest != other.deleteUserRequest) return false
            if (imageBytes != null) {
                if (other.imageBytes == null) return false
                if (!imageBytes.contentEquals(other.imageBytes)) return false
            } else if (other.imageBytes != null) {
                return false
            }

            return true
        }

        override fun hashCode(): Int {
            var result = isRefreshing.hashCode()
            result = 31 * result + showImagePicker.hashCode()
            result = 31 * result + userRequest.hashCode()
            result = 31 * result + logoutRequest.hashCode()
            result = 31 * result + deleteUserRequest.hashCode()
            result = 31 * result + (permissionDialog?.hashCode() ?: 0)
            result = 31 * result + (imageBytes?.contentHashCode() ?: 0)
            return result
        }
    }

    sealed interface Intent {
        data object LoadProfile : Intent

        data object Logout : Intent

        data object RefreshProfile : Intent

        data class ViewAddressDetails(
            val address: Address,
        ) : Intent

        data object HideImagePickerBottomSheet : Intent

        data class OnImageClick(
            val userId: Int,
            val isDelete: Boolean,
        ) : Intent

        data class CheckImagePermissions(
            val permission: AppPermission,
            val userId: Int,
        ) : Intent

        data object ClosePermissionsDialog : Intent

        data class ConfirmPermissionAction(
            val type: PermissionDialogActionType,
            val userId: Int,
        ) : Intent

        data class DeleteUser(
            val userId: Int,
        ) : Intent
    }

    sealed interface Effect {
        data object NavigateToLogin : Effect

        data class NavigateToAddressDetails(
            val address: Address,
        ) : Effect

        data class ShowSnackBarError(
            val snackBarData: SnackBarData,
        ) : Effect
    }

    sealed interface UserRequestState {
        data object Initial : UserRequestState

        data object Loading : UserRequestState

        data class Success(
            val user: User,
        ) : UserRequestState

        data class Error(
            val error: UiError,
        ) : UserRequestState
    }

    sealed interface LogoutRequestState {
        data object Initial : LogoutRequestState

        data object Loading : LogoutRequestState
    }

    sealed interface DeleteUserRequestState {
        data object Initial : DeleteUserRequestState

        data object Loading : DeleteUserRequestState
    }
}

/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.presentation.profile

import com.iolandarosa.retailhub.core.ui.error.UiError
import com.iolandarosa.retailhub.core.ui.images.PermissionType
import com.iolandarosa.retailhub.features.auth.domain.model.Address
import com.iolandarosa.retailhub.features.auth.domain.model.User

interface ProfileContract {
    data class State(
        val userRequest: UserRequestState = UserRequestState.Initial,
        val logoutRequest: LogoutRequestState = LogoutRequestState.Initial,
        val isRefreshing: Boolean = false,
        val showImagePicker: Boolean = false,
    ) {
        val isInteractionEnabled: Boolean
            get() =
                userRequest !is UserRequestState.Loading &&
                    !isRefreshing &&
                    logoutRequest !is LogoutRequestState.Loading
    }

    sealed interface Intent {
        data object LoadProfile : Intent

        data object Logout : Intent

        data object RefreshProfile : Intent

        data class ViewAddressDetails(
            val address: Address,
        ) : Intent

        data class ShowImagePickerBottomSheet(
            val show: Boolean,
        ) : Intent

        data class CheckImagePermissions(
            val permissionType: PermissionType,
        ) : Intent
    }

    sealed interface Effect {
        data object NavigateToLogin : Effect

        data class NavigateToAddressDetails(
            val address: Address,
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
}

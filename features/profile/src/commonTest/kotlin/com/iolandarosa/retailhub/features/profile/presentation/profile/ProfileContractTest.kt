/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.presentation.profile

import com.iolandarosa.retailhub.core.ui.error.UiError
import com.iolandarosa.retailhub.core.ui.permissions.PermissionDialog
import com.iolandarosa.retailhub.core.ui.permissions.PermissionDialogActionType
import com.iolandarosa.retailhub.features.profile.utils.TestUser
import retailhub.features.profile.generated.resources.Res
import retailhub.features.profile.generated.resources.camera_permission_rational_confirm_btn
import retailhub.features.profile.generated.resources.camera_permission_rational_description
import retailhub.features.profile.generated.resources.camera_permission_rational_title
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ProfileContractTest {
    @Test
    fun initialState_isInteractionEnabled_expectsTrue() {
        val state = ProfileContract.State()

        assertTrue(state.isInteractionEnabled)
        assertIs<ProfileContract.UserRequestState.Initial>(state.userRequest)
        assertIs<ProfileContract.LogoutRequestState.Initial>(state.logoutRequest)
        assertIs<ProfileContract.DeleteUserRequestState.Initial>(state.deleteUserRequest)
        assertFalse(state.isRefreshing)
        assertFalse(state.showImagePicker)
        assertNull(state.permissionDialog)
        assertFalse(state.showPermissionsDialog)
        assertFalse(state.showLogoutLoading)
        assertFalse(state.showDeleteLoading)
    }

    @Test
    fun hasPermissionDialog_showPermissionsDialog_expectsTrue() {
        val state =
            ProfileContract.State(
                permissionDialog =
                    PermissionDialog(
                        titleId = Res.string.camera_permission_rational_title,
                        descriptionId = Res.string.camera_permission_rational_description,
                        confirmButtonLabelId = Res.string.camera_permission_rational_confirm_btn,
                        type = PermissionDialogActionType.CameraRational,
                    ),
            )

        assertTrue(state.showPermissionsDialog)
    }

    @Test
    fun userRequestLoading_isInteractionEnabled_expectsFalse() {
        val state = ProfileContract.State(userRequest = ProfileContract.UserRequestState.Loading)

        assertFalse(state.isInteractionEnabled)
        assertIs<ProfileContract.LogoutRequestState.Initial>(state.logoutRequest)
        assertFalse(state.isRefreshing)
    }

    @Test
    fun userRequestError_isInteractionEnabled_expectsTrue() {
        val state = ProfileContract.State(userRequest = ProfileContract.UserRequestState.Error(UiError()))

        assertTrue(state.isInteractionEnabled)
        assertIs<ProfileContract.LogoutRequestState.Initial>(state.logoutRequest)
        assertFalse(state.isRefreshing)
    }

    @Test
    fun userRequestSuccess_isInteractionEnabled_expectsTrue() {
        val state = ProfileContract.State(userRequest = ProfileContract.UserRequestState.Success(TestUser.user))

        assertTrue(state.isInteractionEnabled)
        assertIs<ProfileContract.LogoutRequestState.Initial>(state.logoutRequest)
        assertFalse(state.isRefreshing)
    }

    @Test
    fun userRequestLoadingAndLogoutRequestLoading_isInteractionEnabled_expectsFalse() {
        val state =
            ProfileContract.State(
                userRequest = ProfileContract.UserRequestState.Loading,
                logoutRequest = ProfileContract.LogoutRequestState.Loading,
            )

        assertFalse(state.isInteractionEnabled)
        assertFalse(state.isRefreshing)
    }

    @Test
    fun userRequestErrorAndLogoutRequestLoading_isInteractionEnabled_expectsFalse() {
        val state =
            ProfileContract.State(
                userRequest = ProfileContract.UserRequestState.Error(UiError()),
                logoutRequest = ProfileContract.LogoutRequestState.Loading,
            )

        assertFalse(state.isInteractionEnabled)
        assertFalse(state.isRefreshing)
    }

    @Test
    fun userRequestSuccessAndLogoutRequestLoading_isInteractionEnabled_expectsFalse() {
        val state =
            ProfileContract.State(
                userRequest = ProfileContract.UserRequestState.Success(TestUser.user),
                logoutRequest = ProfileContract.LogoutRequestState.Loading,
            )

        assertFalse(state.isInteractionEnabled)
        assertFalse(state.isRefreshing)
    }

    @Test
    fun isRefreshing_isInteractionEnabled_expectsFalse() {
        val state = ProfileContract.State(isRefreshing = true)

        assertFalse(state.isInteractionEnabled)
        assertIs<ProfileContract.LogoutRequestState.Initial>(state.logoutRequest)
        assertIs<ProfileContract.UserRequestState.Initial>(state.userRequest)
    }

    @Test
    fun deleteUserRequestLoading_isInteractionEnabled_expectsFalse() {
        val state =
            ProfileContract.State(
                deleteUserRequest = ProfileContract.DeleteUserRequestState.Loading,
            )

        assertFalse(state.isInteractionEnabled)
        assertTrue(state.showDeleteLoading)
    }

    @Test
    fun deleteUserRequestInitial_showDeleteLoading_expectsFalse() {
        val state =
            ProfileContract.State(
                deleteUserRequest = ProfileContract.DeleteUserRequestState.Initial,
            )

        assertFalse(state.showDeleteLoading)
    }

    @Test
    fun logoutRequestLoading_showLogoutLoading_expectsTrue() {
        val state =
            ProfileContract.State(
                logoutRequest = ProfileContract.LogoutRequestState.Loading,
            )

        assertTrue(state.showLogoutLoading)
    }
}

/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.presentation.profile

import com.iolandarosa.retailhub.core.ui.error.UiError
import com.iolandarosa.retailhub.features.auth.presentation.profile.ProfileContract.LogoutRequestState
import com.iolandarosa.retailhub.features.auth.presentation.profile.ProfileContract.State
import com.iolandarosa.retailhub.features.auth.presentation.profile.ProfileContract.UserRequestState
import com.iolandarosa.retailhub.features.auth.utils.TestUser
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ProfileContractTest {
    @Test
    fun initialState_isInteractionEnabled_expectsTrue() {
        val state = State()

        assertTrue(state.isInteractionEnabled)
        assertIs<UserRequestState.Initial>(state.userRequest)
        assertIs<LogoutRequestState.Initial>(state.logoutRequest)
        assertFalse(state.isRefreshing)
        assertFalse(state.showImagePicker)
    }

    @Test
    fun userRequestLoading_isInteractionEnabled_expectsFalse() {
        val state = State(userRequest = UserRequestState.Loading)

        assertFalse(state.isInteractionEnabled)
        assertIs<LogoutRequestState.Initial>(state.logoutRequest)
        assertFalse(state.isRefreshing)
    }

    @Test
    fun userRequestError_isInteractionEnabled_expectsTrue() {
        val state = State(userRequest = UserRequestState.Error(UiError()))

        assertTrue(state.isInteractionEnabled)
        assertIs<LogoutRequestState.Initial>(state.logoutRequest)
        assertFalse(state.isRefreshing)
    }

    @Test
    fun userRequestSuccess_isInteractionEnabled_expectsTrue() {
        val state = State(userRequest = UserRequestState.Success(TestUser.user))

        assertTrue(state.isInteractionEnabled)
        assertIs<LogoutRequestState.Initial>(state.logoutRequest)
        assertFalse(state.isRefreshing)
    }

    @Test
    fun userRequestLoadingAndLogoutRequestLoading_isInteractionEnabled_expectsFalse() {
        val state = State(userRequest = UserRequestState.Loading, logoutRequest = LogoutRequestState.Loading)

        assertFalse(state.isInteractionEnabled)
        assertFalse(state.isRefreshing)
    }

    @Test
    fun userRequestErrorAndLogoutRequestLoading_isInteractionEnabled_expectsFalse() {
        val state =
            State(userRequest = UserRequestState.Error(UiError()), logoutRequest = LogoutRequestState.Loading)

        assertFalse(state.isInteractionEnabled)
        assertFalse(state.isRefreshing)
    }

    @Test
    fun userRequestSuccessAndLogoutRequestLoading_isInteractionEnabled_expectsFalse() {
        val state =
            State(
                userRequest = UserRequestState.Success(TestUser.user),
                logoutRequest = LogoutRequestState.Loading,
            )

        assertFalse(state.isInteractionEnabled)
        assertFalse(state.isRefreshing)
    }

    @Test
    fun isRefreshing_isInteractionEnabled_expectsFalse() {
        val state = State(isRefreshing = true)

        assertFalse(state.isInteractionEnabled)
        assertIs<LogoutRequestState.Initial>(state.logoutRequest)
        assertIs<UserRequestState.Initial>(state.userRequest)
    }
}

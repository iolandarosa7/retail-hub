/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.presentation.profile

import com.iolandarosa.retailhub.core.ui.error.UiError
import com.iolandarosa.retailhub.features.auth.utils.TestUser
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ProfileUiStateTest {
    @Test
    fun initialState_isInteractionEnabled_expectsTrue() {
        val state = ProfileUiState()

        assertTrue(state.isInteractionEnabled)
        assertIs<UserRequestState.Initial>(state.userRequest)
        assertIs<LogoutRequestState.Initial>(state.logoutRequest)
    }

    @Test
    fun userRequestLoading_isInteractionEnabled_expectsFalse() {
        val state = ProfileUiState(userRequest = UserRequestState.Loading)

        assertFalse(state.isInteractionEnabled)
        assertIs<LogoutRequestState.Initial>(state.logoutRequest)
    }

    @Test
    fun userRequestError_isInteractionEnabled_expectsTrue() {
        val state = ProfileUiState(userRequest = UserRequestState.Error(UiError()))

        assertTrue(state.isInteractionEnabled)
        assertIs<LogoutRequestState.Initial>(state.logoutRequest)
    }

    @Test
    fun userRequestSuccess_isInteractionEnabled_expectsTrue() {
        val state = ProfileUiState(userRequest = UserRequestState.Success(TestUser.user))

        assertTrue(state.isInteractionEnabled)
        assertIs<LogoutRequestState.Initial>(state.logoutRequest)
    }

    @Test
    fun userRequestLoadingAndLogoutRequestLoading_isInteractionEnabled_expectsFalse() {
        val state = ProfileUiState(userRequest = UserRequestState.Loading, logoutRequest = LogoutRequestState.Loading)

        assertFalse(state.isInteractionEnabled)
    }

    @Test
    fun userRequestErrorAndLogoutRequestLoading_isInteractionEnabled_expectsFalse() {
        val state =
            ProfileUiState(userRequest = UserRequestState.Error(UiError()), logoutRequest = LogoutRequestState.Loading)

        assertFalse(state.isInteractionEnabled)
    }

    @Test
    fun userRequestSuccessAndLogoutRequestLoading_isInteractionEnabled_expectsFalse() {
        val state =
            ProfileUiState(
                userRequest = UserRequestState.Success(TestUser.user),
                logoutRequest = LogoutRequestState.Loading,
            )

        assertFalse(state.isInteractionEnabled)
    }
}

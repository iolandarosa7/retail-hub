/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.presentation.login

import com.iolandarosa.retailhub.core.ui.error.UiError
import com.iolandarosa.retailhub.core.ui.form.FormState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LoginContractTest {
    private val formState = FormState(fields = emptyList())

    @Test
    fun initialState_isInteractionEnabledAndError_expectsTrueAndNull() {
        val state = LoginContract.State(formState = formState)

        assertTrue(state.isInteractionEnabled)
        assertNull(state.error)
        assertIs<LoginContract.RequestState.Initial>(state.loginRequest)
    }

    @Test
    fun loginRequestLoading_isInteractionEnabledAndError_expectsFalseAndNull() {
        val state =
            LoginContract.State(
                formState = formState,
                loginRequest = LoginContract.RequestState.Loading,
            )

        assertFalse(state.isInteractionEnabled)
        assertNull(state.error)
    }

    @Test
    fun loginRequestSuccess_isInteractionEnabledAndError_expectsFalseAndNull() {
        val state =
            LoginContract.State(
                formState = formState,
                loginRequest = LoginContract.RequestState.Success,
            )

        assertTrue(state.isInteractionEnabled)
        assertNull(state.error)
    }

    @Test
    fun loginRequestError_isInteractionEnabledAndError_expectsFalseAndErrorValue() {
        val error = UiError(description = "error")

        val state =
            LoginContract.State(
                formState = formState,
                loginRequest = LoginContract.RequestState.Error(error),
            )

        assertTrue(state.isInteractionEnabled)
        assertEquals(error, state.error)
    }
}

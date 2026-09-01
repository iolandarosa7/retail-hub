/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.login

import com.iolandarosa.retailhub.core.ui.error.UiError
import com.iolandarosa.retailhub.core.ui.form.FormState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LoginUiStateTest {
    private val formState = FormState(fields = emptyList())

    @Test
    fun initialStateEnablesInteractionAndHasNoError() {
        val state = LoginUiState(formState = formState)

        assertTrue(state.isInteractionEnabled)
        assertNull(state.error)
        assertIs<LoginRequestState.Initial>(state.loginRequest)
    }

    @Test
    fun loadingStateDisablesInteractionAndHasNoError() {
        val state =
            LoginUiState(
                formState = formState,
                loginRequest = LoginRequestState.Loading,
            )

        assertFalse(state.isInteractionEnabled)
        assertNull(state.error)
    }

    @Test
    fun successStateEnablesInteractionAndHasNoError() {
        val state =
            LoginUiState(
                formState = formState,
                loginRequest = LoginRequestState.Success,
            )

        assertTrue(state.isInteractionEnabled)
        assertNull(state.error)
    }

    @Test
    fun errorStateEnablesInteractionAndExposesError() {
        val error = UiError(description = "error")

        val state =
            LoginUiState(
                formState = formState,
                loginRequest = LoginRequestState.Error(error),
            )

        assertTrue(state.isInteractionEnabled)
        assertEquals(error, state.error)
    }
}

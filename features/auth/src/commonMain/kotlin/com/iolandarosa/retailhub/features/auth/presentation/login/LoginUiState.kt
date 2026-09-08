/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.presentation.login

import com.iolandarosa.retailhub.core.ui.error.UiError
import com.iolandarosa.retailhub.core.ui.form.FormState

sealed interface LoginRequestState {
    data object Initial : LoginRequestState

    data object Loading : LoginRequestState

    data object Success : LoginRequestState

    data class Error(
        val error: UiError,
    ) : LoginRequestState
}

data class LoginUiState(
    val formState: FormState,
    val loginRequest: LoginRequestState = LoginRequestState.Initial,
) {
    val isInteractionEnabled: Boolean get() = loginRequest !is LoginRequestState.Loading
    val error: UiError? get() = (loginRequest as? LoginRequestState.Error)?.error
}

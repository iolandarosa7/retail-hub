package com.iolandarosa.retailhub.features.auth.presentation.login

import com.iolandarosa.retailhub.core.forms.FormState
import com.iolandarosa.retailhub.core.models.UiError

sealed class LoginRequestState {
    data object Initial: LoginRequestState()
    data object Loading: LoginRequestState()
    data object Success: LoginRequestState()

    data class Error(val error: UiError) : LoginRequestState()
}

data class LoginUiState(
    val formState: FormState,
    val loginRequest: LoginRequestState = LoginRequestState.Initial,
)

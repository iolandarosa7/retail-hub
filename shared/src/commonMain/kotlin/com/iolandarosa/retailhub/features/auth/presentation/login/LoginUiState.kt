package com.iolandarosa.retailhub.features.auth.presentation.login

import com.iolandarosa.retailhub.core.models.FormFieldData
import com.iolandarosa.retailhub.core.models.UiError

sealed class LoginRequestState {
    data object Initial: LoginRequestState()
    data object Loading: LoginRequestState()
    data object Success: LoginRequestState()

    data class Error(val error: UiError) : LoginRequestState()
}

data class LoginUiState(
    val username: FormFieldData<String> = FormFieldData(value = ""),
    val password: FormFieldData<String> = FormFieldData(value = ""),
    val loginRequest: LoginRequestState = LoginRequestState.Initial,
)

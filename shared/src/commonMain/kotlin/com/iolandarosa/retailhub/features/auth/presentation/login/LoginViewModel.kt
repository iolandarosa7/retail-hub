package com.iolandarosa.retailhub.features.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iolandarosa.retailhub.core.extensions.toUiError
import com.iolandarosa.retailhub.core.models.FormFieldData
import com.iolandarosa.retailhub.core.models.NetworkResult
import com.iolandarosa.retailhub.features.auth.domain.usecase.LoginUseCase
import com.iolandarosa.retailhub.core.validation.ValidationError
import com.iolandarosa.retailhub.core.validation.ValidationResult
import com.iolandarosa.retailhub.core.validation.Validators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retailhub.shared.generated.resources.Res
import retailhub.shared.generated.resources.requiredField

class LoginViewModel(
    private val loginUseCase: LoginUseCase
): ViewModel() {
    private val _state: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState())
    val state = _state.asStateFlow()

    fun onUsernameChanged(value: String) {
        _state.value = _state.value.copy(username = FormFieldData(value))
    }

    fun onPasswordChanged(value: String) {
        _state.value = _state.value.copy(password = FormFieldData(value))
    }

    private fun validateForm(username: String, password: String): Boolean {
        val usernameResult = Validators.validateRequired(username)
        val passwordResult = Validators.validateRequired(password)

        _state.value = _state.value.copy(
            username = _state.value.username.copy(
                errorStringId = if (usernameResult is ValidationResult.Invalid) Res.string.requiredField else null
            ),
            password = _state.value.password.copy(
                errorStringId = if (passwordResult is ValidationResult.Invalid) Res.string.requiredField else null
            )
        )

        return usernameResult is ValidationResult.Valid && passwordResult is ValidationResult.Valid
    }

    fun login() {
        val username = _state.value.username.value.trim()
        val password = _state.value.password.value.trim()

        if (!validateForm(username, password)) {
            return
        }

        _state.value = _state.value.copy(loginRequest = LoginRequestState.Loading)

        viewModelScope.launch {
            when(val response = loginUseCase(username = username, password = password)) {
                is NetworkResult.Failure -> {
                    _state.value = _state.value.copy(loginRequest = LoginRequestState.Error(error = response.toUiError()))
                }
                is NetworkResult.Success -> _state.value = _state.value.copy(loginRequest = LoginRequestState.Success)
            }
        }
    }
}
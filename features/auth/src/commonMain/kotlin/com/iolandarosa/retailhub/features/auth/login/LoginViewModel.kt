package com.iolandarosa.retailhub.features.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.core.ui.form.FormState
import com.iolandarosa.retailhub.core.ui.extension.toUiError
import com.iolandarosa.retailhub.features.auth.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
): ViewModel() {
    private val _state: MutableStateFlow<LoginUiState> = MutableStateFlow(
        LoginUiState(
            formState = FormState(
                LoginForm.get(
                    onActionDone = { login() }
                )
            )
        )
    )
    val state = _state.asStateFlow()

    fun login() {
        val formState = _state.value.formState
        if (!formState.isFormValid()) return

        val username = formState.getFieldDataByName<String>(LoginForm.USERNAME) ?: ""
        val password = formState.getFieldDataByName<String>(LoginForm.PASSWORD) ?: ""

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
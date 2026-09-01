package com.iolandarosa.retailhub.features.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iolandarosa.retailhub.core.common.dispatcher.DispatcherProvider
import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.core.ui.extension.toUiError
import com.iolandarosa.retailhub.core.ui.form.FormState
import com.iolandarosa.retailhub.features.auth.domain.usecase.LoginUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {
    private val _state: MutableStateFlow<LoginUiState> = MutableStateFlow(
        LoginUiState(
            formState = FormState(
                fields = LoginForm.get(
                    onValueChanged = { onIntent(LoginIntent.OnFormFieldChanged) },
                    onActionDone = { onIntent(LoginIntent.OnLoginClicked) }
                )
            )
        )
    )
    val state = _state.asStateFlow()

    private val _effects = Channel<LoginEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onIntent(intent: LoginIntent) {
        when (intent) {
            LoginIntent.OnFormFieldChanged -> resetError()
            LoginIntent.OnLoginClicked -> login()
        }
    }

    private fun resetError() {
        if (_state.value.loginRequest !is LoginRequestState.Initial) {
            _state.update { it.copy(loginRequest = LoginRequestState.Initial) }
        }
    }

    private fun login() {
        val formState = _state.value.formState
        if (!formState.isFormValid()) return

        _state.update { it.copy(loginRequest = LoginRequestState.Loading) }

        viewModelScope.launch(dispatcherProvider.main) {
            val username = formState.getFieldDataByName<String>(LoginForm.USERNAME) ?: ""
            val password = formState.getFieldDataByName<String>(LoginForm.PASSWORD) ?: ""

            when (val response = loginUseCase(username = username, password = password)) {
                is NetworkResult.Failure -> {
                    _state.update { it.copy(loginRequest = LoginRequestState.Error(error = response.toUiError())) }
                }

                is NetworkResult.Success -> {
                    _state.update { it.copy(loginRequest = LoginRequestState.Success) }
                    _effects.send(LoginEffect.NavigateToProfile)
                }
            }
        }
    }
}

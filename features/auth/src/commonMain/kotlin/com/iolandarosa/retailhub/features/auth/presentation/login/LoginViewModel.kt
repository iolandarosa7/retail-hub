/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iolandarosa.retailhub.core.common.dispatcher.DispatcherProvider
import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.core.ui.extension.toUiError
import com.iolandarosa.retailhub.core.ui.form.FormState
import com.iolandarosa.retailhub.features.auth.domain.interactors.LoginUseCase
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
    private val _state: MutableStateFlow<LoginContract.State> =
        MutableStateFlow(
            LoginContract.State(
                formState =
                    FormState(
                        fields =
                            LoginForm.get(
                                onValueChanged = { onIntent(LoginContract.Intent.OnFormFieldChanged) },
                                onActionDone = { onIntent(LoginContract.Intent.OnLoginClicked) },
                            ),
                    ),
            ),
        )
    val state = _state.asStateFlow()

    private val _effects = Channel<LoginContract.Effect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onIntent(intent: LoginContract.Intent) {
        when (intent) {
            LoginContract.Intent.OnFormFieldChanged -> resetError()
            LoginContract.Intent.OnLoginClicked -> login()
        }
    }

    private fun resetError() {
        if (_state.value.loginRequest !is LoginContract.RequestState.Initial) {
            _state.update { it.copy(loginRequest = LoginContract.RequestState.Initial) }
        }
    }

    private fun login() {
        val formState = _state.value.formState
        if (!formState.isFormValid()) return

        _state.update { it.copy(loginRequest = LoginContract.RequestState.Loading) }

        viewModelScope.launch(dispatcherProvider.main) {
            val username = formState.getFieldDataByName<String>(LoginForm.USERNAME) ?: ""
            val password = formState.getFieldDataByName<String>(LoginForm.PASSWORD) ?: ""

            when (val result = loginUseCase(username = username, password = password)) {
                is NetworkResult.Failure -> {
                    _state.update {
                        it.copy(
                            loginRequest = LoginContract.RequestState.Error(error = result.toUiError()),
                        )
                    }
                }

                is NetworkResult.Success -> {
                    _state.update { it.copy(loginRequest = LoginContract.RequestState.Success) }
                    _effects.send(LoginContract.Effect.NavigateToProfile)
                }
            }
        }
    }
}

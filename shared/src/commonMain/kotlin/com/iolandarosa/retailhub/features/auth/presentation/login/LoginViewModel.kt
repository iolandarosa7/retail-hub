package com.iolandarosa.retailhub.features.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iolandarosa.retailhub.core.models.NetworkResult
import com.iolandarosa.retailhub.features.auth.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
): ViewModel() {
    private val _state: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState.Initial)
    val state = _state.asStateFlow()

    fun login(username: String, password: String) {
        _state.value = LoginUiState.Loading

        viewModelScope.launch {
            val response = loginUseCase(username = username, password = password)

            when(response) {
                is NetworkResult.Failure -> _state.value = LoginUiState.Error
                is NetworkResult.Success -> _state.value = LoginUiState.Success
            }
        }
    }
}
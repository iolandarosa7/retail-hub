package com.iolandarosa.retailhub.features.auth.presentation.login

sealed class LoginUiState {
    data object Initial: LoginUiState()
    data object Loading: LoginUiState()
    data object Error: LoginUiState()
    data object Success: LoginUiState()
}
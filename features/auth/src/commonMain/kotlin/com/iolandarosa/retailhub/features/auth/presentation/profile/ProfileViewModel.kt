/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iolandarosa.retailhub.core.common.dispatcher.DispatcherProvider
import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.core.ui.extension.toUiError
import com.iolandarosa.retailhub.features.auth.domain.interactors.GetAuthUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getAuthUserUseCase: GetAuthUserUseCase,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {
    private val _state: MutableStateFlow<ProfileUiState> =
        MutableStateFlow(ProfileUiState())
    val state = _state.asStateFlow()

    fun onIntent(intent: ProfileIntent) {
        when (intent) {
            ProfileIntent.LoadProfile -> getAuthUser()
        }
    }

    private fun getAuthUser() {
        if (state.value.userRequest is UserRequestState.Loading) return

        _state.update { it.copy(userRequest = UserRequestState.Loading) }

        viewModelScope.launch(dispatcherProvider.main) {
            when (val result = getAuthUserUseCase()) {
                is NetworkResult.Failure -> {
                    _state.update { it.copy(userRequest = UserRequestState.Error(error = result.toUiError())) }
                }

                is NetworkResult.Success -> {
                    _state.update { it.copy(userRequest = UserRequestState.Success(result.data)) }
                }
            }
        }
    }
}

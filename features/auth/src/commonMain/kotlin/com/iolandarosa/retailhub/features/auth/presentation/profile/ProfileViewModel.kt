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
import com.iolandarosa.retailhub.features.auth.domain.interactors.LogoutUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getAuthUserUseCase: GetAuthUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {
    private val _state: MutableStateFlow<ProfileUiState> =
        MutableStateFlow(ProfileUiState())
    val state = _state.asStateFlow()

    private val _effects = Channel<ProfileEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onIntent(intent: ProfileIntent) {
        when (intent) {
            ProfileIntent.LoadProfile -> getAuthUser(isRefresh = false)
            ProfileIntent.Logout -> logout()
            ProfileIntent.RefreshProfile -> getAuthUser(isRefresh = true)
        }
    }

    private fun getAuthUser(isRefresh: Boolean) {
        if (state.value.userRequest is UserRequestState.Loading) return
        if (isRefresh && state.value.isRefreshing) return

        _state.update {
            if (isRefresh) {
                it.copy(isRefreshing = true)
            } else {
                it.copy(userRequest = UserRequestState.Loading)
            }
        }

        viewModelScope.launch(dispatcherProvider.main) {
            when (val result = getAuthUserUseCase()) {
                is NetworkResult.Failure.Unauthorized -> {
                    _state.update { it.copy(userRequest = UserRequestState.Initial, isRefreshing = false) }

                    _effects.send(ProfileEffect.NavigateToLogin)
                }

                is NetworkResult.Failure -> {
                    _state.update {
                        it.copy(userRequest = UserRequestState.Error(result.toUiError()), isRefreshing = false)
                    }
                }

                is NetworkResult.Success -> {
                    _state.update { it.copy(userRequest = UserRequestState.Success(result.data), isRefreshing = false) }
                }
            }
        }
    }

    private fun logout() {
        if (state.value.logoutRequest is LogoutRequestState.Loading) return

        _state.update { it.copy(logoutRequest = LogoutRequestState.Loading) }

        viewModelScope.launch(dispatcherProvider.main) {
            logoutUseCase()
            _state.update { it.copy(logoutRequest = LogoutRequestState.Initial) }
            _effects.send(ProfileEffect.NavigateToLogin)
        }
    }
}

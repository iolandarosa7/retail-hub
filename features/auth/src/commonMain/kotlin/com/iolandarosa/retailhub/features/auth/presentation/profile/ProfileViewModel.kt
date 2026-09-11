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
import com.iolandarosa.retailhub.core.ui.images.PermissionType
import com.iolandarosa.retailhub.features.auth.domain.interactors.GetAuthUserUseCase
import com.iolandarosa.retailhub.features.auth.domain.interactors.LogoutUseCase
import com.iolandarosa.retailhub.features.auth.domain.model.Address
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
    private val _state: MutableStateFlow<ProfileContract.State> =
        MutableStateFlow(ProfileContract.State())
    val state = _state.asStateFlow()

    private val _effects = Channel<ProfileContract.Effect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onIntent(intent: ProfileContract.Intent) {
        when (intent) {
            ProfileContract.Intent.LoadProfile -> {
                getAuthUser(isRefresh = false)
            }

            ProfileContract.Intent.Logout -> {
                logout()
            }

            ProfileContract.Intent.RefreshProfile -> {
                getAuthUser(isRefresh = true)
            }

            is ProfileContract.Intent.ViewAddressDetails -> {
                viewAddressDetails(intent.address)
            }

            is ProfileContract.Intent.ShowImagePickerBottomSheet -> {
                _state.update { it.copy(showImagePicker = intent.show) }
            }

            is ProfileContract.Intent.CheckImagePermissions -> {
                checkImagePermissions(intent.permissionType)
            }
        }
    }

    private fun getAuthUser(isRefresh: Boolean) {
        if (state.value.userRequest is ProfileContract.UserRequestState.Loading) return
        if (isRefresh && state.value.isRefreshing) return

        _state.update {
            if (isRefresh) {
                it.copy(isRefreshing = true)
            } else {
                it.copy(userRequest = ProfileContract.UserRequestState.Loading)
            }
        }

        viewModelScope.launch(dispatcherProvider.main) {
            when (val result = getAuthUserUseCase()) {
                is NetworkResult.Failure.Unauthorized -> {
                    _state.update {
                        it.copy(
                            userRequest = ProfileContract.UserRequestState.Initial,
                            isRefreshing = false,
                        )
                    }

                    _effects.send(ProfileContract.Effect.NavigateToLogin)
                }

                is NetworkResult.Failure -> {
                    _state.update {
                        it.copy(
                            userRequest = ProfileContract.UserRequestState.Error(result.toUiError()),
                            isRefreshing = false,
                        )
                    }
                }

                is NetworkResult.Success -> {
                    _state.update {
                        it.copy(
                            userRequest = ProfileContract.UserRequestState.Success(result.data),
                            isRefreshing = false,
                        )
                    }
                }
            }
        }
    }

    private fun logout() {
        if (state.value.logoutRequest is ProfileContract.LogoutRequestState.Loading) return

        _state.update { it.copy(logoutRequest = ProfileContract.LogoutRequestState.Loading) }

        viewModelScope.launch(dispatcherProvider.main) {
            logoutUseCase()
            _state.update { it.copy(logoutRequest = ProfileContract.LogoutRequestState.Initial) }
            _effects.send(ProfileContract.Effect.NavigateToLogin)
        }
    }

    private fun viewAddressDetails(address: Address) {
        viewModelScope.launch(dispatcherProvider.main) {
            _effects.send(ProfileContract.Effect.NavigateToAddressDetails(address))
        }
    }

    private fun checkImagePermissions(permissionType: PermissionType) {
        _state.update { it.copy(showImagePicker = false) }
        // todo will call a permission manager
    }
}

/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iolandarosa.retailhub.core.common.dispatcher.DispatcherProvider
import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.core.user.domain.interactors.GetAuthUserUseCase
import com.iolandarosa.retailhub.core.user.domain.interactors.GetLocalUserImageUseCase
import com.iolandarosa.retailhub.features.home.domain.model.AuthUserImage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getAuthUserUseCase: GetAuthUserUseCase,
    private val dispatcherProvider: DispatcherProvider,
    private val getLocalUserImageUseCase: GetLocalUserImageUseCase,
) : ViewModel() {
    private val _state: MutableStateFlow<HomeContract.State> =
        MutableStateFlow(HomeContract.State())
    val state = _state.asStateFlow()

    private val _effects = Channel<HomeContract.Effect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onIntent(intent: HomeContract.Intent) {
        when (intent) {
            HomeContract.Intent.LoadAuthUserImage -> {
                loadAuthUserImage()
            }

            HomeContract.Intent.OnProfileClick -> {
                viewModelScope.launch(dispatcherProvider.main) {
                    _effects.send(HomeContract.Effect.NavigateUserProfile)
                }
            }
        }
    }

    private fun loadAuthUserImage() {
        viewModelScope.launch(dispatcherProvider.main) {
            when (val response = getAuthUserUseCase()) {
                is NetworkResult.Success -> {
                    val imageBytes = getLocalUserImageUseCase(response.data.id)

                    _state.update {
                        it.copy(
                            authUserImage = AuthUserImage(
                                url = response.data.image,
                                bytes = imageBytes,
                            ),
                        )
                    }
                }

                else -> {}
            }
        }
    }
}

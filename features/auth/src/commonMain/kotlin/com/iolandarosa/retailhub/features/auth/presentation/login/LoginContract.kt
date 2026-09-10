/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.presentation.login

import com.iolandarosa.retailhub.core.ui.error.UiError
import com.iolandarosa.retailhub.core.ui.form.FormState

interface LoginContract {
    data class State(
        val formState: FormState,
        val loginRequest: RequestState = RequestState.Initial,
    ) {
        val isInteractionEnabled: Boolean get() = loginRequest !is RequestState.Loading
        val error: UiError? get() = (loginRequest as? RequestState.Error)?.error
    }

    sealed interface Intent {
        data object OnFormFieldChanged : Intent
        data object OnLoginClicked : Intent
    }

    sealed interface Effect {
        data object NavigateToProfile : Effect
    }

    sealed interface RequestState {
        data object Initial : RequestState
        data object Loading : RequestState
        data object Success : RequestState
        data class Error(val error: UiError) : RequestState
    }
}

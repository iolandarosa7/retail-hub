/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.presentation.login

sealed interface LoginIntent {
    data object OnFormFieldChanged : LoginIntent

    data object OnLoginClicked : LoginIntent
}

sealed interface LoginEffect {
    data object NavigateToProfile : LoginEffect
}

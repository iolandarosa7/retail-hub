/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.presentation.profile

sealed interface ProfileIntent {
    data object LoadProfile : ProfileIntent

    data object Logout : ProfileIntent

    data object RefreshProfile : ProfileIntent
}

sealed interface ProfileEffect {
    data object NavigateToLogin : ProfileEffect
}

/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.home.presentation

import com.iolandarosa.retailhub.features.home.domain.model.AuthUserImage

interface HomeContract {
    data class State(
        val authUserImage: AuthUserImage = AuthUserImage(),
    )

    sealed interface Intent {
        data object LoadAuthUserImage : Intent

        data object ClickProfile : Intent
    }

    sealed interface Effect {
        data object NavigateUserProfile : Effect
    }
}

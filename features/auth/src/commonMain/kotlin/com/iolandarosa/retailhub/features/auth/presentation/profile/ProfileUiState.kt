/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.presentation.profile

import com.iolandarosa.retailhub.core.ui.error.UiError
import com.iolandarosa.retailhub.features.auth.domain.model.User

sealed interface UserRequestState {
    data object Initial : UserRequestState

    data object Loading : UserRequestState

    data class Success(
        val user: User,
    ) : UserRequestState

    data class Error(
        val error: UiError,
    ) : UserRequestState
}

data class ProfileUiState(
    val userRequest: UserRequestState = UserRequestState.Initial,
)

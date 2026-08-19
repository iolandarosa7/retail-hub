package com.iolandarosa.retailhub.features.auth.login

import com.iolandarosa.retailhub.core.ui.error.UiError

sealed interface LoginIntent {
    data object OnFormFieldChanged: LoginIntent
    data object OnLoginClicked: LoginIntent
}

/* todo implement when navigation exists

sealed interface LoginEffect {
    data object NavigateToHome : LoginEffect
}*/
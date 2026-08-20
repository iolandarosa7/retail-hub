package com.iolandarosa.retailhub.features.auth.login

sealed interface LoginIntent {
    data object OnFormFieldChanged: LoginIntent
    data object OnLoginClicked: LoginIntent
}
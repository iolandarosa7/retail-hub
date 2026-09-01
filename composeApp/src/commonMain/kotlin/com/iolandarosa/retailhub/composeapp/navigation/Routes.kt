package com.iolandarosa.retailhub.composeapp.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface AppRoute : NavKey

@Serializable
data object LoginRoute : AppRoute

@Serializable
data object ProfileRoute : AppRoute

/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.composeapp.navigation

import androidx.navigation3.runtime.NavKey
import com.iolandarosa.retailhub.features.profile.domain.model.Address
import kotlinx.serialization.Serializable

@Serializable
sealed interface AppRoute : NavKey

@Serializable
data object LoginRoute : AppRoute

@Serializable
data object ProfileRoute : AppRoute

@Serializable
data class AddressRoute(
    val address: Address,
) : AppRoute

/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.composeapp.navigation

import retailhub.composeapp.generated.resources.Res
import retailhub.composeapp.generated.resources.address_details

fun AppRoute.appBarConfig(): AppBarConfig? =
    when (this) {
        is AddressRoute -> {
            AppBarConfig(
                titleRes = Res.string.address_details,
                showBack = true,
            )
        }

        LoginRoute -> {
            null
        }

        ProfileRoute -> {
            null
        }
    }

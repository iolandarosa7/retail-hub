/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.composeapp.navigation

import com.iolandarosa.retailhub.core.user.domain.model.Address
import com.iolandarosa.retailhub.core.user.domain.model.Coordinates
import retailhub.composeapp.generated.resources.Res
import retailhub.composeapp.generated.resources.address_details
import retailhub.composeapp.generated.resources.my_profile
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class RoutesExtensionsTest {
    @Test
    fun addressRoute_appBarConfig_hasExpectedValue() {
        val config =
            AddressRoute(
                address =
                    Address(
                        street = "address",
                        city = "city",
                        state = "state",
                        stateCode = "stateCode",
                        postalCode = "postalCode",
                        coordinates = Coordinates(lat = 1.0, lng = 1.0),
                        country = "country",
                    ),
            ).appBarConfig()

        assertEquals(Res.string.address_details, config?.titleRes)
        assertEquals(true, config?.showBack)
    }

    @Test
    fun homeRoute_appBarConfig_hasNullValue() {
        assertNull(HomeRoute.appBarConfig())
    }

    @Test
    fun profileRoute_appBarConfig_hasNullValue() {
        val config = ProfileRoute.appBarConfig()
        assertEquals(Res.string.my_profile, config?.titleRes)
        assertEquals(true, config?.showBack)
    }
}

package com.iolandarosa.retailhub.composeapp.navigation

import com.iolandarosa.retailhub.features.auth.domain.model.Address
import com.iolandarosa.retailhub.features.auth.domain.model.Coordinates
import retailhub.composeapp.generated.resources.Res
import retailhub.composeapp.generated.resources.address_details
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class RoutesExtensionsTest {
    @Test
    fun addressRoute_appBarConfig_hasExpectedValue() {
        val config = AddressRoute(
            address = Address(
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
    fun loginRoute_appBarConfig_hasNullValue() {
        assertNull(LoginRoute.appBarConfig())
    }

    @Test
    fun profileRoute_appBarConfig_hasNullValue() {
        assertNull(ProfileRoute.appBarConfig())
    }
}

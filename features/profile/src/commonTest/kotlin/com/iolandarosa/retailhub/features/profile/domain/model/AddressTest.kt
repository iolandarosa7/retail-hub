/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals

class AddressTest {
    @Test
    fun addressInstance_hasExpectedValues() {
        val expectedCoordinates = Coordinates(lat = 40.7128, lng = -74.0060)
        val address =
            Address(
                street = "123 Main St",
                city = "New York",
                state = "New York",
                stateCode = "NY",
                postalCode = "10001",
                coordinates = expectedCoordinates,
                country = "USA",
            )

        assertEquals("123 Main St", address.street)
        assertEquals("New York", address.city)
        assertEquals("New York", address.state)
        assertEquals("NY", address.stateCode)
        assertEquals("10001", address.postalCode)
        assertEquals(expectedCoordinates, address.coordinates)
        assertEquals("USA", address.country)
    }
}

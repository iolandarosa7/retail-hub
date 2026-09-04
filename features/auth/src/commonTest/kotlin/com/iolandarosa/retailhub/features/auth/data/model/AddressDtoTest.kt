/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.data.model

import kotlin.test.Test
import kotlin.test.assertEquals

class AddressDtoTest {
    @Test
    fun addressDtoInstance_hasExpectedValues() {
        val coordinates = CoordinatesDto(lat = 1.0, lng = 2.0)
        val addressDto =
            AddressDto(
                address = "123 Main St",
                city = "New York",
                state = "New York",
                stateCode = "NY",
                postalCode = "10001",
                coordinates = coordinates,
                country = "USA",
            )

        assertEquals("123 Main St", addressDto.address)
        assertEquals("New York", addressDto.city)
        assertEquals("New York", addressDto.state)
        assertEquals("NY", addressDto.stateCode)
        assertEquals("10001", addressDto.postalCode)
        assertEquals(coordinates, addressDto.coordinates)
        assertEquals("USA", addressDto.country)
    }
}

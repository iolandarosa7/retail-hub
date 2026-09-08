/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.data.model

import kotlin.test.Test
import kotlin.test.assertEquals

class CompanyDtoTest {
    @Test
    fun companyDtoInstance_hasExpectedValues() {
        val coordinates = CoordinatesDto(lat = 1.0, lng = 2.0)
        val address =
            AddressDto(
                address = "123 Main St",
                city = "New York",
                state = "New York",
                stateCode = "NY",
                postalCode = "10001",
                coordinates = coordinates,
                country = "USA",
            )
        val companyDto =
            CompanyDto(
                department = "Engineering",
                name = "Tech Corp",
                title = "Senior Engineer",
                address = address,
            )

        assertEquals("Engineering", companyDto.department)
        assertEquals("Tech Corp", companyDto.name)
        assertEquals("Senior Engineer", companyDto.title)
        assertEquals(address, companyDto.address)
    }
}

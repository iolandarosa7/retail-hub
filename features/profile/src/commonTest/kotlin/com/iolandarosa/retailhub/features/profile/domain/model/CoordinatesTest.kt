/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals

class CoordinatesTest {
    @Test
    fun coordinatesInstance_hasExpectedValues() {
        val coordinates = Coordinates(lat = 40.7128, lng = -74.0060)

        assertEquals(40.7128, coordinates.lat)
        assertEquals(-74.0060, coordinates.lng)
    }
}

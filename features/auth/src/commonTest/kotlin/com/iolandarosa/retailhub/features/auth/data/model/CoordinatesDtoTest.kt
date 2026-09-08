/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.data.model

import kotlin.test.Test
import kotlin.test.assertEquals

class CoordinatesDtoTest {
    @Test
    fun coordinatesDtoInstance_hasExpectedValues() {
        val coordinatesDto =
            CoordinatesDto(
                lat = 1.0,
                lng = 2.0,
            )

        assertEquals(1.0, coordinatesDto.lat)
        assertEquals(2.0, coordinatesDto.lng)
    }
}

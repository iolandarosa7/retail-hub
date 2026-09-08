/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.data.model

import kotlin.test.Test
import kotlin.test.assertEquals

class HairDtoTest {
    @Test
    fun hairDtoInstance_hasExpectedValues() {
        val hairDto =
            HairDto(
                color = "Black",
                type = "Straight",
            )

        assertEquals("Black", hairDto.color)
        assertEquals("Straight", hairDto.type)
    }
}

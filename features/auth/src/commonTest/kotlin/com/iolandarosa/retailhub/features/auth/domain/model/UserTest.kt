/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals

class UserTest {
    @Test
    fun userInstance_hasExpectedValues() {
        val user =
            User(
                id = 1,
                name = "name",
            )

        assertEquals(1, user.id)
        assertEquals("name", user.name)
    }
}

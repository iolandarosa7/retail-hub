package com.iolandarosa.retailhub.features.auth.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals

class UserTest {
    @Test
    fun `User instance has expected values`() {
        val user = User(
            id = 1,
            name = "name"
        )

        assertEquals(1, user.id)
        assertEquals("name", user.name)
    }
}
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
                name = "John Doe",
                image = "image_url",
                role = "admin",
                email = "john@example.com",
                phone = "123456",
                age = 30,
                gender = "male",
                birthDate = "2000-01-01",
                bloodGroup = "A+",
                height = 180.0,
                weight = 80.0,
                eyeColor = "brown",
                hairColor = "black",
                hairType = "straight",
                address = "123 Main St",
                city = "Lisbon",
                country = "Portugal",
            )

        assertEquals("John Doe", user.name)
        assertEquals("image_url", user.image)
        assertEquals("admin", user.role)
        assertEquals("john@example.com", user.email)
        assertEquals("123456", user.phone)
        assertEquals(30, user.age)
        assertEquals("male", user.gender)
        assertEquals("2000-01-01", user.birthDate)
        assertEquals("A+", user.bloodGroup)
        assertEquals(180.0, user.height)
        assertEquals(80.0, user.weight)
        assertEquals("brown", user.eyeColor)
        assertEquals("black", user.hairColor)
        assertEquals("straight", user.hairType)
        assertEquals("123 Main St", user.address)
        assertEquals("Lisbon", user.city)
        assertEquals("Portugal", user.country)
    }
}

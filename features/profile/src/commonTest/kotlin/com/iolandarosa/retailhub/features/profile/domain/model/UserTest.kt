/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals

class UserTest {
    @Test
    fun userInstance_hasExpectedValues() {
        val expectedAddress =
            Address(
                street = "address",
                city = "city",
                state = "state",
                stateCode = "stateCode",
                postalCode = "postalCode",
                coordinates = Coordinates(lat = 1.0, lng = 1.0),
                country = "country",
            )

        val user =
            User(
                id = 1,
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
                address = expectedAddress,
            )

        assertEquals(1, user.id)
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
        assertEquals(expectedAddress, user.address)
    }
}

package com.iolandarosa.retailhub.features.auth.data.model

import kotlin.test.Test
import kotlin.test.assertEquals

class UserDtoTest {
    @Test
    fun `UserDto instance has expected values`() {
        val userDto = UserDto(
            id = 1,
            username = "username",
            email = "email",
            firstName = "firstName",
            lastName = "lastName",
            gender = "gender",
            image = "image",
            accessToken = "accessToken",
            refreshToken = "refreshToken"
        )

        assertEquals(1, userDto.id)
        assertEquals("username", userDto.username)
        assertEquals("email", userDto.email)
        assertEquals("firstName", userDto.firstName)
        assertEquals("lastName", userDto.lastName)
        assertEquals("gender", userDto.gender)
        assertEquals("image", userDto.image)
        assertEquals("accessToken", userDto.accessToken)
        assertEquals("refreshToken", userDto.refreshToken)
    }
}
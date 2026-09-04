/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.data.model

import kotlin.test.Test
import kotlin.test.assertEquals

class AuthenticationDtoTest {
    @Test
    fun authenticatedDtoInstance_hasExpectedValues() {
        val authenticationDto =
            AuthenticationDto(
                id = 1,
                username = "username",
                email = "email",
                firstName = "firstName",
                lastName = "lastName",
                gender = "gender",
                image = "image",
                accessToken = "accessToken",
                refreshToken = "refreshToken",
            )

        assertEquals(1, authenticationDto.id)
        assertEquals("username", authenticationDto.username)
        assertEquals("email", authenticationDto.email)
        assertEquals("firstName", authenticationDto.firstName)
        assertEquals("lastName", authenticationDto.lastName)
        assertEquals("gender", authenticationDto.gender)
        assertEquals("image", authenticationDto.image)
        assertEquals("accessToken", authenticationDto.accessToken)
        assertEquals("refreshToken", authenticationDto.refreshToken)
    }
}

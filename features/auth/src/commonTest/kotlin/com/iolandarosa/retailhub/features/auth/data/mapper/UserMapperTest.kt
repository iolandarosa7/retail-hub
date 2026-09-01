/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.data.mapper

import com.iolandarosa.retailhub.features.auth.data.model.UserDto
import kotlin.test.Test
import kotlin.test.assertEquals

class UserMapperTest {
    @Test
    fun userDtoMapperExecutionHasExpectedResult() {
        val userDto =
            UserDto(
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

        val user = userDto.toDomain()

        assertEquals(1, user.id)
        assertEquals("firstName lastName", user.name)
    }
}

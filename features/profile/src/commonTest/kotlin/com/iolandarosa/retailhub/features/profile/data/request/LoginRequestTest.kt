/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.data.request

import kotlin.test.Test
import kotlin.test.assertEquals

class LoginRequestTest {
    @Test
    fun loginRequestInstance_hasExpectedValues() {
        val loginRequest =
            LoginRequest(
                username = "username",
                password = "password",
                expiresInMins = 1,
            )

        assertEquals("username", loginRequest.username)
        assertEquals("password", loginRequest.password)
        assertEquals(1, loginRequest.expiresInMins)
    }
}

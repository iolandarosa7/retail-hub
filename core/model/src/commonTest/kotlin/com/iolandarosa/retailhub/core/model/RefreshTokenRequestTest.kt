package com.iolandarosa.retailhub.core.model

import kotlin.test.Test
import kotlin.test.assertEquals

class RefreshTokenRequestTest {
    @Test
    fun requestInstance_hasExpectedValues() {
        val refreshToken = "refreshToken"
        val expiresInMins = 5

        val request = RefreshTokenRequest(refreshToken, expiresInMins)
        assertEquals(refreshToken, request.refreshToken)
        assertEquals(expiresInMins, request.expiresInMins)
    }
}
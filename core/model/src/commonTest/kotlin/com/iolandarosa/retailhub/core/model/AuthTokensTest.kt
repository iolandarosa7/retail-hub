package com.iolandarosa.retailhub.core.model

import kotlin.test.Test
import kotlin.test.assertEquals

class AuthTokensTest {
    @Test
    fun modelInstance_hasExpectedValues() {
        val accessToken = "accessToken"
        val refreshToken = "refreshToken"

        val model = AuthTokens(accessToken, refreshToken)
        assertEquals(accessToken, model.accessToken)
        assertEquals(refreshToken, model.refreshToken)
    }
}
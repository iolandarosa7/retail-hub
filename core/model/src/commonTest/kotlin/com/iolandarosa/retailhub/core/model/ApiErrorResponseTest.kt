package com.iolandarosa.retailhub.core.model

import kotlin.test.Test
import kotlin.test.assertEquals

class ApiErrorResponseTest {
    @Test
    fun responseInstance_hasExpectedValues() {
        val expectedMessage = "message"

        val response = ApiErrorResponse(expectedMessage)
        assertEquals(expectedMessage, response.message)
    }
}
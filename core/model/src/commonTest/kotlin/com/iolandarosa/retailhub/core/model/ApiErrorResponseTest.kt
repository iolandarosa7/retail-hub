package com.iolandarosa.retailhub.core.model

import kotlin.test.Test
import kotlin.test.assertEquals

class ApiErrorResponseTest {
    @Test
    fun `creates response with given message`() {
        val expectedMessage = "message"

        val response = ApiErrorResponse(expectedMessage)
        assertEquals(expectedMessage, response.message)
    }
}
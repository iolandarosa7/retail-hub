package com.iolandarosa.retailhub.core.ui.error

import retailhub.core.ui.generated.resources.Res
import retailhub.core.ui.generated.resources.error_server
import retailhub.core.ui.generated.resources.error_unknown
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UiErrorTest {
    @Test
    fun `creates error with default values`() {
        val error = UiError()

        assertNull(error.description)
        assertEquals(Res.string.error_unknown, error.descriptionId)
    }

    @Test
    fun `creates error with provided description`() {
        val error = UiError(description = "Something went wrong")

        assertEquals("Something went wrong", error.description)
        assertEquals(Res.string.error_unknown, error.descriptionId)
    }

    @Test
    fun `creates error with provided description resource`() {
        val error = UiError(
            description = "Something went wrong",
            descriptionId = Res.string.error_server
        )

        assertEquals("Something went wrong", error.description)
        assertEquals(Res.string.error_server, error.descriptionId)
    }
}
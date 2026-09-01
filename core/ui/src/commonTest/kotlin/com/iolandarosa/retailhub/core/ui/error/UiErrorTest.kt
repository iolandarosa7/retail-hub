/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.error

import retailhub.core.ui.generated.resources.Res
import retailhub.core.ui.generated.resources.error_server
import retailhub.core.ui.generated.resources.error_unknown
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UiErrorTest {
    @Test
    fun createsErrorWithDefaultValues() {
        val error = UiError()

        assertNull(error.description)
        assertEquals(Res.string.error_unknown, error.descriptionId)
    }

    @Test
    fun createsErrorWithProvidedDescription() {
        val error = UiError(description = "Something went wrong")

        assertEquals("Something went wrong", error.description)
        assertEquals(Res.string.error_unknown, error.descriptionId)
    }

    @Test
    fun createsErrorWithProvidedDescriptionResource() {
        val error =
            UiError(
                description = "Something went wrong",
                descriptionId = Res.string.error_server,
            )

        assertEquals("Something went wrong", error.description)
        assertEquals(Res.string.error_server, error.descriptionId)
    }
}

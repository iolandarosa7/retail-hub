/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.error

import retailhub.core.ui.generated.resources.Res
import retailhub.core.ui.generated.resources.error_server
import retailhub.core.ui.generated.resources.error_server_title
import retailhub.core.ui.generated.resources.error_unknown
import retailhub.core.ui.generated.resources.error_unknown_title
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UiErrorTest {
    @Test
    fun default_instance_hasExpectedValues() {
        val error = UiError()

        assertNull(error.description)
        assertEquals(Res.string.error_unknown, error.descriptionId)
        assertEquals(Res.string.error_unknown_title, error.titleId)
    }

    @Test
    fun description_instance_hasExpectedValues() {
        val error = UiError(description = "Something went wrong")

        assertEquals("Something went wrong", error.description)
        assertEquals(Res.string.error_unknown, error.descriptionId)
        assertEquals(Res.string.error_unknown_title, error.titleId)
    }

    @Test
    fun descriptionIdAndTitleId_instance_hasExpectedValues() {
        val error =
            UiError(
                description = "Something went wrong",
                descriptionId = Res.string.error_server,
                titleId = Res.string.error_server_title,
            )

        assertEquals("Something went wrong", error.description)
        assertEquals(Res.string.error_server, error.descriptionId)
        assertEquals(Res.string.error_server_title, error.titleId)
    }
}

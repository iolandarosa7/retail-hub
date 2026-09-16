/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.snackbar

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SnackBarDataTest {
    @Test
    fun defaultValues_instance_hasExpectedValues() {
        val data = SnackBarData()

        assertNull(data.messageId)
        assertEquals("", data.message)
        assertEquals(SnackBarType.INFO, data.type)
    }

    @Test
    fun customValues_instance_hasExpectedValues() {
        val data =
            SnackBarData(
                messageId = null,
                message = "Success message",
                type = SnackBarType.SUCCESS,
            )

        assertNull(data.messageId)
        assertEquals("Success message", data.message)
        assertEquals(SnackBarType.SUCCESS, data.type)
    }
}

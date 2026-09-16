/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.snackbar

import androidx.compose.material3.SnackbarDuration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

class AppSnackBarVisualsTest {

    @Test
    fun defaultValues_instance_hasExpectedValues() {
        val visuals = AppSnackBarVisuals(
            messageId = null,
            message = "Test message",
            type = SnackBarType.INFO
        )

        assertNull(visuals.messageId)
        assertEquals("Test message", visuals.message)
        assertEquals(SnackBarType.INFO, visuals.type)
        assertNull(visuals.actionLabel)
        assertFalse(visuals.withDismissAction)
        assertEquals(SnackbarDuration.Short, visuals.duration)
    }

    @Test
    fun customValues_instance_hasExpectedValues() {
        val visuals = AppSnackBarVisuals(
            messageId = null,
            message = "Error occurred",
            type = SnackBarType.ERROR,
            actionLabel = "Retry",
            withDismissAction = true,
            duration = SnackbarDuration.Long
        )

        assertNull(visuals.messageId)
        assertEquals("Error occurred", visuals.message)
        assertEquals(SnackBarType.ERROR, visuals.type)
        assertEquals("Retry", visuals.actionLabel)
        assertEquals(true, visuals.withDismissAction)
        assertEquals(SnackbarDuration.Long, visuals.duration)
    }
}

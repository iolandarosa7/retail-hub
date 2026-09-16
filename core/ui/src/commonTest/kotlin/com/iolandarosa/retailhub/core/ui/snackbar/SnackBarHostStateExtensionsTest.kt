/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.snackbar

import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SnackBarHostStateExtensionsTest {

    @Test
    fun hostState_showSnackBar_hasCorrectValues() = runTest {
        val snackBarHostState = SnackbarHostState()

        val data = SnackBarData(
            messageId = null,
            message = "Hello Test",
            type = SnackBarType.SUCCESS
        )

        val job = launch {
            snackBarHostState.showSnackBar(data)
        }

        yield()

        val currentVisuals = snackBarHostState.currentSnackbarData?.visuals as? AppSnackBarVisuals
        assertNotNull(currentVisuals)
        assertEquals("Hello Test", currentVisuals.message)
        assertEquals(SnackBarType.SUCCESS, currentVisuals.type)

        job.cancel()
    }
}

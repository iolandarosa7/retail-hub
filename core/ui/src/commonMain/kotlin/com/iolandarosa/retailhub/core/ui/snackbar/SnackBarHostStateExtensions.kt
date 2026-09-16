/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.snackbar

import androidx.compose.material3.SnackbarHostState

suspend fun SnackbarHostState.showSnackBar(data: SnackBarData) {
    showSnackbar(
        visuals =
            AppSnackBarVisuals(
                messageId = data.messageId,
                message = data.message,
                type = data.type,
            ),
    )
}

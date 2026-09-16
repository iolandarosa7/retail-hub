/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.snackbar

import org.jetbrains.compose.resources.StringResource

data class SnackBarData(
    val messageId: StringResource? = null,
    val message: String = "",
    val type: SnackBarType = SnackBarType.INFO,
)

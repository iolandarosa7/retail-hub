/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.error

import org.jetbrains.compose.resources.StringResource
import retailhub.core.ui.generated.resources.Res
import retailhub.core.ui.generated.resources.error_unknown
import retailhub.core.ui.generated.resources.error_unknown_title

data class UiError(
    val titleId: StringResource = Res.string.error_unknown_title,
    val description: String? = null,
    val descriptionId: StringResource = Res.string.error_unknown,
    val hasRetry: Boolean = true,
)

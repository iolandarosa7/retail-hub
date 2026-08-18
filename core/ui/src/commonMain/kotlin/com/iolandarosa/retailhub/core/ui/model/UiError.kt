package com.iolandarosa.retailhub.core.ui.model

import org.jetbrains.compose.resources.StringResource
import retailhub.core.ui.generated.resources.Res
import retailhub.core.ui.generated.resources.error_unknown

data class UiError(
    val description: String? = null,
    val descriptionId: StringResource = Res.string.error_unknown
)
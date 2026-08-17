package com.iolandarosa.retailhub.core.models

import org.jetbrains.compose.resources.StringResource
import retailhub.shared.generated.resources.Res
import retailhub.shared.generated.resources.error_unknown

data class UiError(
    val description: String? = null,
    val descriptionId: StringResource = Res.string.error_unknown
)

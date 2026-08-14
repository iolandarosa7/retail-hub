package com.iolandarosa.retailhub.core.models

import org.jetbrains.compose.resources.StringResource
data class FormFieldData<T>(
    val value: T,
    val errorStringId: StringResource? = null,
)

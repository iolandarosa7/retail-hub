package com.iolandarosa.retailhub.core.models

import kotlinx.serialization.Serializable

@Serializable
data class ApiErrorResponse(val message: String)
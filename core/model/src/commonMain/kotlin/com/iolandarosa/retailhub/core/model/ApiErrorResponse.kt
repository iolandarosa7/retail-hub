package com.iolandarosa.retailhub.core.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiErrorResponse(val message: String)
package com.iolandarosa.retailhub.core.common.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiErrorResponse(val message: String)
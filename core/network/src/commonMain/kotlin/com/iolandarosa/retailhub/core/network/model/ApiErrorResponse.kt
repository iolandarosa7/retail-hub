package com.iolandarosa.retailhub.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiErrorResponse(val message: String)
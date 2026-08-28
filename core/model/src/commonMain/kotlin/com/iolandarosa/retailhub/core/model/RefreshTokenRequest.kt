package com.iolandarosa.retailhub.core.model

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequest(
    val refreshToken: String,
    val expiresInMins: Int,
)

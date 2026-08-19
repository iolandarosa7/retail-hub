package com.iolandarosa.retailhub.features.auth.data.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val username: String,
    val password: String,
    val expiresInMins: Int, // default 60 mins
)
package com.iolandarosa.retailhub.features.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CryptoDto(
    val coin: String,
    val wallet: String,
    val network: String,
)

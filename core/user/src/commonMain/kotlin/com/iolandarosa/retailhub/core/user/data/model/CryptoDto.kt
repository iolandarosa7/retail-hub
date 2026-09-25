/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.user.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CryptoDto(
    val coin: String,
    val wallet: String,
    val network: String,
)

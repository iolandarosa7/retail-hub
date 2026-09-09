/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class HairDto(
    val color: String,
    val type: String,
)

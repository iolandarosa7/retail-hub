/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.data.model

import kotlinx.serialization.Serializable

@Serializable
data class HairDto(
    val color: String,
    val type: String,
)

/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Coordinates(
    val lat: Double,
    val lng: Double,
)

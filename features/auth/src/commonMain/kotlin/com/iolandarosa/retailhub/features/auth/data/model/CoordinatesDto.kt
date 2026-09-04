package com.iolandarosa.retailhub.features.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CoordinatesDto(
    val lat: Double,
    val lng: Double,
)

package com.iolandarosa.retailhub.features.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AddressDto(
    val address: String,
    val city: String,
    val state: String,
    val stateCode: String,
    val postalCode: String,
    val coordinates: CoordinatesDto,
    val country: String,
)

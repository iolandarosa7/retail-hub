/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val street: String,
    val city: String,
    val state: String,
    val stateCode: String,
    val postalCode: String,
    val coordinates: Coordinates,
    val country: String,
)

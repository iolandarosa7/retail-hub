/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CompanyDto(
    val department: String,
    val name: String,
    val title: String,
    val address: AddressDto,
)

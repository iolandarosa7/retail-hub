/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.user.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CompanyDto(
    val department: String,
    val name: String,
    val title: String,
    val address: AddressDto,
)

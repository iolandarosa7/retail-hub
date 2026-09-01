/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.network

import kotlinx.serialization.Serializable

@Serializable
data class TestDto(
    val id: Int,
    val name: String,
)

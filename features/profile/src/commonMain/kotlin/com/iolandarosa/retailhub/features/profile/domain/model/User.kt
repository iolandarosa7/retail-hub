/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.domain.model

data class User(
    val id: Int,
    val name: String,
    val image: String,
    val role: String,
    val email: String,
    val phone: String,
    val age: Int,
    val gender: String,
    val birthDate: String,
    val bloodGroup: String,
    val height: Double,
    val weight: Double,
    val eyeColor: String,
    val hairColor: String,
    val hairType: String,
    val address: Address,
)

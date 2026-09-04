package com.iolandarosa.retailhub.features.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val maidenName: String,
    val age: Int,
    val gender: String,
    val email: String,
    val phone: String,
    val username: String,
    val password: String,
    val birthDate: String,
    val image: String,
    val bloodGroup: String,
    val height: Double,
    val weight: Double,
    val eyeColor: String,
    val hair: HairDto,
    val ip: String,
    val address: AddressDto,
    val macAddress: String,
    val university: String,
    val bank: BankDto,
    val company: CompanyDto,
    val ein: String,
    val ssn: String,
    val userAgent: String,
    val crypto: CryptoDto,
    val role: String,
)

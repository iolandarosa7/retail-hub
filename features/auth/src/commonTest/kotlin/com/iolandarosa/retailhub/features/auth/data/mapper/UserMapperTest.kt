/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.data.mapper

import com.iolandarosa.retailhub.features.auth.data.model.AddressDto
import com.iolandarosa.retailhub.features.auth.data.model.BankDto
import com.iolandarosa.retailhub.features.auth.data.model.CompanyDto
import com.iolandarosa.retailhub.features.auth.data.model.CoordinatesDto
import com.iolandarosa.retailhub.features.auth.data.model.CryptoDto
import com.iolandarosa.retailhub.features.auth.data.model.HairDto
import com.iolandarosa.retailhub.features.auth.data.model.UserDto
import com.iolandarosa.retailhub.features.auth.domain.model.Address
import com.iolandarosa.retailhub.features.auth.domain.model.Coordinates
import kotlin.test.Test
import kotlin.test.assertEquals

class UserMapperTest {
    @Test
    fun userDto_toDomains_hasExpectedResult() {
        val userDto =
            UserDto(
                id = 1,
                username = "username",
                email = "email",
                firstName = "firstName",
                lastName = "lastName",
                gender = "gender",
                image = "image",
                maidenName = "maidenName",
                age = 1,
                phone = "phone",
                password = "password",
                birthDate = "birthDate",
                bloodGroup = "bloodGroup",
                height = 40.0,
                weight = 1.64,
                eyeColor = "eyeColor",
                hair = HairDto(color = "color", type = "type"),
                ip = "ip",
                address =
                    AddressDto(
                        address = "address",
                        city = "city",
                        state = "state",
                        stateCode = "stateCode",
                        postalCode = "postalCode",
                        coordinates = CoordinatesDto(lat = 1.0, lng = 1.0),
                        country = "country",
                    ),
                macAddress = "macAddress",
                university = "university",
                bank =
                    BankDto(
                        cardExpire = "cardExpire",
                        cardNumber = "cardNumber",
                        cardType = "cardType",
                        currency = "currency",
                        iban = "iban",
                    ),
                company =
                    CompanyDto(
                        department = "department",
                        name = "name",
                        address =
                            AddressDto(
                                address = "address",
                                city = "city",
                                state = "state",
                                stateCode = "stateCode",
                                postalCode = "postalCode",
                                coordinates = CoordinatesDto(lat = 1.0, lng = 1.0),
                                country = "country",
                            ),
                        title = "title",
                    ),
                ein = "ein",
                ssn = "ssn",
                userAgent = "userAgent",
                crypto =
                    CryptoDto(
                        coin = "coin",
                        wallet = "wallet",
                        network = "network",
                    ),
                role = "role",
            )

        val user = userDto.toDomain()
        val expectedAddress =
            Address(
                street = "address",
                city = "city",
                state = "state",
                stateCode = "stateCode",
                postalCode = "postalCode",
                coordinates = Coordinates(lat = 1.0, lng = 1.0),
                country = "country",
            )

        assertEquals("firstName lastName", user.name)
        assertEquals("image", user.image)
        assertEquals("role", user.role)
        assertEquals("email", user.email)
        assertEquals("phone", user.phone)
        assertEquals(1, user.age)
        assertEquals("gender", user.gender)
        assertEquals("birthDate", user.birthDate)
        assertEquals("bloodGroup", user.bloodGroup)
        assertEquals(40.0, user.height)
        assertEquals(1.64, user.weight)
        assertEquals("eyeColor", user.eyeColor)
        assertEquals("color", user.hairColor)
        assertEquals("type", user.hairType)
        assertEquals(expectedAddress, user.address)
    }
}

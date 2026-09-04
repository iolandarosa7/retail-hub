/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.data.model

import kotlin.test.Test
import kotlin.test.assertEquals

class UserDtoTest {
    @Test
    fun userDtoInstance_hasExpectedValues() {
        val hair = HairDto(color = "Black", type = "Straight")
        val coordinates = CoordinatesDto(lat = 1.0, lng = 2.0)
        val address =
            AddressDto(
                address = "123 Main St",
                city = "New York",
                state = "New York",
                stateCode = "NY",
                postalCode = "10001",
                coordinates = coordinates,
                country = "USA",
            )
        val bank =
            BankDto(
                cardExpire = "12/26",
                cardNumber = "1234567890123456",
                cardType = "Visa",
                currency = "USD",
                iban = "US1234567890",
            )
        val company =
            CompanyDto(
                department = "Engineering",
                name = "Tech Corp",
                title = "Senior Engineer",
                address = address,
            )
        val crypto =
            CryptoDto(
                coin = "Bitcoin",
                wallet = "0x123",
                network = "Mainnet",
            )

        val userDto =
            UserDto(
                id = 1,
                firstName = "John",
                lastName = "Doe",
                maidenName = "Smith",
                age = 30,
                gender = "male",
                email = "john.doe@example.com",
                phone = "1234567890",
                username = "johndoe",
                password = "password123",
                birthDate = "1994-01-01",
                image = "image_url",
                bloodGroup = "A+",
                height = 180.0,
                weight = 75.0,
                eyeColor = "Brown",
                hair = hair,
                ip = "127.0.0.1",
                address = address,
                macAddress = "00:00:00:00:00:00",
                university = "State University",
                bank = bank,
                company = company,
                ein = "12-3456789",
                ssn = "000-00-0000",
                userAgent = "Mozilla/5.0",
                crypto = crypto,
                role = "admin",
            )

        assertEquals(1, userDto.id)
        assertEquals("John", userDto.firstName)
        assertEquals("Doe", userDto.lastName)
        assertEquals("Smith", userDto.maidenName)
        assertEquals(30, userDto.age)
        assertEquals("male", userDto.gender)
        assertEquals("john.doe@example.com", userDto.email)
        assertEquals("1234567890", userDto.phone)
        assertEquals("johndoe", userDto.username)
        assertEquals("password123", userDto.password)
        assertEquals("1994-01-01", userDto.birthDate)
        assertEquals("image_url", userDto.image)
        assertEquals("A+", userDto.bloodGroup)
        assertEquals(180.0, userDto.height)
        assertEquals(75.0, userDto.weight)
        assertEquals("Brown", userDto.eyeColor)
        assertEquals(hair, userDto.hair)
        assertEquals("127.0.0.1", userDto.ip)
        assertEquals(address, userDto.address)
        assertEquals("00:00:00:00:00:00", userDto.macAddress)
        assertEquals("State University", userDto.university)
        assertEquals(bank, userDto.bank)
        assertEquals(company, userDto.company)
        assertEquals("12-3456789", userDto.ein)
        assertEquals("000-00-0000", userDto.ssn)
        assertEquals("Mozilla/5.0", userDto.userAgent)
        assertEquals(crypto, userDto.crypto)
        assertEquals("admin", userDto.role)
    }
}

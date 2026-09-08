/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.data.model

import kotlin.test.Test
import kotlin.test.assertEquals

class BankDtoTest {
    @Test
    fun bankDtoInstance_hasExpectedValues() {
        val bankDto =
            BankDto(
                cardExpire = "12/26",
                cardNumber = "1234567890123456",
                cardType = "Visa",
                currency = "USD",
                iban = "US1234567890",
            )

        assertEquals("12/26", bankDto.cardExpire)
        assertEquals("1234567890123456", bankDto.cardNumber)
        assertEquals("Visa", bankDto.cardType)
        assertEquals("USD", bankDto.currency)
        assertEquals("US1234567890", bankDto.iban)
    }
}

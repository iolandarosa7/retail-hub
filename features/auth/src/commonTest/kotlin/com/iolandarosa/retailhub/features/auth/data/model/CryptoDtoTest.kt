/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.data.model

import kotlin.test.Test
import kotlin.test.assertEquals

class CryptoDtoTest {
    @Test
    fun cryptoDtoInstance_hasExpectedValues() {
        val cryptoDto =
            CryptoDto(
                coin = "Bitcoin",
                wallet = "0x123",
                network = "Mainnet",
            )

        assertEquals("Bitcoin", cryptoDto.coin)
        assertEquals("0x123", cryptoDto.wallet)
        assertEquals("Mainnet", cryptoDto.network)
    }
}

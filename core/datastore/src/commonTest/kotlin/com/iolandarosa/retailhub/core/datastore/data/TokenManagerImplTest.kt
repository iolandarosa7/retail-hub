package com.iolandarosa.retailhub.core.datastore.data

import com.iolandarosa.retailhub.core.datastore.FakeDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TokenManagerImplTest {
    private lateinit var testDataStore: FakeDataStore
    private lateinit var tokenManager: TokenManagerImpl

    @BeforeTest
    fun setup() {
        testDataStore = FakeDataStore()
        tokenManager = TokenManagerImpl(testDataStore.dataStore)
    }

    @AfterTest
    fun tearDown() {
        testDataStore.cleanup()
    }

    @Test
    fun withTokensInfo_getAccessToken_returnsExpectedValue() = runTest {
        val accessToken = "accessToken"

        tokenManager.saveAuthTokens(accessToken, "refreshToken")

        assertEquals(accessToken, tokenManager.getAccessToken().first())
    }

    @Test
    fun withEmptyDataStore_getAccessToken_returnsNull() = runTest {
        assertNull(tokenManager.getAccessToken().first())
    }
}
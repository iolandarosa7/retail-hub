package com.iolandarosa.retailhub.core.datastore.data

import com.iolandarosa.retailhub.core.datastore.FakeDataStore
import com.iolandarosa.retailhub.core.model.AuthTokens
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
    fun withTokensInfo_getAuthToken_returnsExpectedValue() = runTest {
        val authTokens = AuthTokens("accessToken", "refreshToken")

        tokenManager.saveAuthTokens(authTokens.accessToken, authTokens.refreshToken)

        assertEquals(authTokens, tokenManager.getAuthTokens().first())
    }

    @Test
    fun withEmptyDataStore_getAccessToken_returnsNull() = runTest {
        assertNull(tokenManager.getAuthTokens().first())
    }

    @Test
    fun withTokensInfo_clearTokens_clearsDatastore() = runTest {
        val authTokens = AuthTokens("accessToken", "refreshToken")

        tokenManager.saveAuthTokens(authTokens.accessToken, authTokens.refreshToken)

        assertEquals(authTokens, tokenManager.getAuthTokens().first())

        tokenManager.clearTokens()

        assertNull(tokenManager.getAuthTokens().first())
    }
}
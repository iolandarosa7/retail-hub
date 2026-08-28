package com.iolandarosa.retailhub.composeapp

import com.iolandarosa.retailhub.core.datastore.domain.TokenManager
import com.iolandarosa.retailhub.core.model.AuthTokens
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.dsl.module

val fakeTestModule = module {
    // overrides TokenManager to avoid creating with the real datastore instance
    single<TokenManager> {
        object : TokenManager {
            override fun getAuthTokens(): Flow<AuthTokens> { return flowOf() }
            override suspend fun saveAuthTokens(accessToken: String, refreshToken: String) {}
            override suspend fun clearTokens() {}
        }
    }
}
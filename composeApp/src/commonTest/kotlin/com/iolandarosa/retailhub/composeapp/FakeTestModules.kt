package com.iolandarosa.retailhub.composeapp

import com.iolandarosa.retailhub.core.datastore.domain.TokenManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.module.Module
import org.koin.dsl.module

val fakeTestModule = module {
    // overrides TokenManager to avoid creating with the real datastore instance
    single<TokenManager> {
        object : TokenManager {
            override fun getAccessToken(): Flow<String?> = flowOf(null)
            override suspend fun saveAuthTokens(accessToken: String, refreshToken: String) {}
        }
    }
}
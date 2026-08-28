package com.iolandarosa.retailhub.core.datastore.domain

import com.iolandarosa.retailhub.core.model.AuthTokens
import kotlinx.coroutines.flow.Flow

interface TokenManager {
    fun getAuthTokens(): Flow<AuthTokens?>
    suspend fun saveAuthTokens(accessToken: String, refreshToken: String)

    suspend fun clearTokens()
}
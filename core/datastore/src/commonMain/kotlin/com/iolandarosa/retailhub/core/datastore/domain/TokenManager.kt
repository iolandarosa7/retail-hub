package com.iolandarosa.retailhub.core.datastore.domain

import kotlinx.coroutines.flow.Flow

interface TokenManager {
    fun getAccessToken(): Flow<String?>
    suspend fun saveAuthTokens(accessToken: String, refreshToken: String)
}
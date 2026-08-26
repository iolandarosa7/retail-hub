package com.iolandarosa.retailhub.features.auth.data.repository

import com.iolandarosa.retailhub.core.datastore.domain.TokenManager
import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.features.auth.data.mapper.toDomain
import com.iolandarosa.retailhub.features.auth.data.request.LoginRequest
import com.iolandarosa.retailhub.features.auth.domain.model.User
import com.iolandarosa.retailhub.features.auth.domain.repository.AuthenticationRepository
import com.iolandarosa.retailhub.features.auth.data.remote.AuthRemoteDataSource

internal class AuthenticationRepositoryImpl(
    private val service: AuthRemoteDataSource,
    private val tokenManager: TokenManager,
) : AuthenticationRepository {
    override suspend fun login(username: String, password: String): NetworkResult<User> {
        val result = service.login(
            LoginRequest(
                username,
                password,
                expiresInMins = 5
            )
        )

        if (result is NetworkResult.Success) {
            tokenManager.saveAuthTokens(
                accessToken = result.data.accessToken,
                refreshToken = result.data.refreshToken
            )
        }

        return result.map {
            it.toDomain()
        }
    }
}
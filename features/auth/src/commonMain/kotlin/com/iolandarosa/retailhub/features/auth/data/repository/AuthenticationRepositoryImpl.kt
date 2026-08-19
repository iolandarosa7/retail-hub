package com.iolandarosa.retailhub.features.auth.data.repository

import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.features.auth.data.mapper.toDomain
import com.iolandarosa.retailhub.features.auth.data.request.LoginRequest
import com.iolandarosa.retailhub.features.auth.domain.model.User
import com.iolandarosa.retailhub.features.auth.domain.repository.AuthenticationRepository
import com.iolandarosa.retailhub.features.auth.data.remote.AuthRemoteDataSource

class AuthenticationRepositoryImpl(private val service: AuthRemoteDataSource): AuthenticationRepository {
    override suspend fun login(username: String, password: String): NetworkResult<User> =
        service.login(
            LoginRequest(
                username,
                password,
                expiresInMins = 5
            )
        ).map { it.toDomain() }
}
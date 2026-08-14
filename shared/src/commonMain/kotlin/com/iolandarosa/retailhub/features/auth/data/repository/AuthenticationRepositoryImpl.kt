package com.iolandarosa.retailhub.features.auth.data.repository

import com.iolandarosa.retailhub.core.models.NetworkResult
import com.iolandarosa.retailhub.features.auth.data.mappers.toDomain
import com.iolandarosa.retailhub.features.auth.data.requests.LoginRequest
import com.iolandarosa.retailhub.features.auth.domain.models.User
import com.iolandarosa.retailhub.features.auth.domain.repository.AuthenticationRepository
import com.iolandarosa.retailhub.features.auth.data.remote.AuthenticationService

class AuthenticationRepositoryImpl(private val service: AuthenticationService):
    AuthenticationRepository {
    override suspend fun login(username: String, password: String): NetworkResult<User> =
        service.login(LoginRequest(username, password, expiresInMins = 5)).map { it.toDomain() }
}
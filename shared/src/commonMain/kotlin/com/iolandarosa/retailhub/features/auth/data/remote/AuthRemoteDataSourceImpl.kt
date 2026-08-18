package com.iolandarosa.retailhub.features.auth.data.remote

import com.iolandarosa.retailhub.core.models.NetworkResult
import com.iolandarosa.retailhub.core.network.Endpoints
import com.iolandarosa.retailhub.features.auth.data.response.UserDto
import com.iolandarosa.retailhub.features.auth.data.request.LoginRequest
import com.iolandarosa.retailhub.core.extensions.safeRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class AuthRemoteDataSourceImpl(
    private val client: HttpClient
): AuthRemoteDataSource {
    override suspend fun login(request: LoginRequest): NetworkResult<UserDto> =
        client.safeRequest {
            post(Endpoints.LOGIN_URL) {
                setBody(request)
            }
        }
}
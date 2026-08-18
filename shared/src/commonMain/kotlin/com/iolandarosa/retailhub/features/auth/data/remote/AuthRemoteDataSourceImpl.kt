package com.iolandarosa.retailhub.features.auth.data.remote

import com.iolandarosa.retailhub.core.common.extensions.safeRequest
import com.iolandarosa.retailhub.core.common.model.NetworkResult
import com.iolandarosa.retailhub.core.network.Endpoints
import com.iolandarosa.retailhub.features.auth.data.response.UserDto
import com.iolandarosa.retailhub.features.auth.data.request.LoginRequest
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
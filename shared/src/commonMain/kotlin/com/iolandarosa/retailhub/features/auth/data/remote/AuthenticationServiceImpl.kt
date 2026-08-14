package com.iolandarosa.retailhub.features.auth.data.remote

import com.iolandarosa.retailhub.core.models.NetworkResult
import com.iolandarosa.retailhub.core.network.Endpoints
import com.iolandarosa.retailhub.features.auth.data.responses.UserDTO
import com.iolandarosa.retailhub.features.auth.data.requests.LoginRequest
import com.iolandarosa.retailhub.core.extensions.safeRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class AuthenticationServiceImpl(
    private val client: HttpClient
): AuthenticationService {
    override suspend fun login(request: LoginRequest): NetworkResult<UserDTO> =
        client.safeRequest {
            post(Endpoints.LOGIN_URL) {
                setBody(request)
            }
        }
}
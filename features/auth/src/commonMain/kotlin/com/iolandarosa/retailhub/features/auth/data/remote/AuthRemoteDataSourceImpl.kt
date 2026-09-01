/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.data.remote

import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.core.network.endpoint.Endpoints
import com.iolandarosa.retailhub.core.network.extensions.safeRequest
import com.iolandarosa.retailhub.features.auth.data.model.UserDto
import com.iolandarosa.retailhub.features.auth.data.request.LoginRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

internal class AuthRemoteDataSourceImpl(
    private val client: HttpClient,
) : AuthRemoteDataSource {
    override suspend fun login(request: LoginRequest): NetworkResult<UserDto> =
        client.safeRequest {
            post(Endpoints.LOGIN_URL) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }
}

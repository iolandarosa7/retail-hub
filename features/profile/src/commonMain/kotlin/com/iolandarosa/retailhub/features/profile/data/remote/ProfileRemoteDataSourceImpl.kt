/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.data.remote

import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.core.network.endpoint.Endpoints
import com.iolandarosa.retailhub.core.network.extensions.invalidateAuthTokens
import com.iolandarosa.retailhub.core.network.extensions.safeRequest
import com.iolandarosa.retailhub.core.user.data.model.UserDto
import com.iolandarosa.retailhub.features.profile.data.model.AuthenticationDto
import com.iolandarosa.retailhub.features.profile.data.request.LoginRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

internal class ProfileRemoteDataSourceImpl(
    private val authenticatedClient: HttpClient,
    private val publicClient: HttpClient,
) : ProfileRemoteDataSource {
    override suspend fun invalidateAuthTokens() {
        authenticatedClient.invalidateAuthTokens()
    }

    override suspend fun deleteUser(userId: Int): NetworkResult<UserDto> =
        authenticatedClient.safeRequest {
            delete("${Endpoints.USERS_URL}/$userId")
        }

    override suspend fun login(request: LoginRequest): NetworkResult<AuthenticationDto> =
        publicClient.safeRequest {
            post(Endpoints.LOGIN_URL) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }
}

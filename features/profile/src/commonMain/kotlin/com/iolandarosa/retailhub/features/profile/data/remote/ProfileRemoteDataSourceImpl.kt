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
import com.iolandarosa.retailhub.features.profile.data.model.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get

internal class ProfileRemoteDataSourceImpl(
    private val authenticatedClient: HttpClient,
) : ProfileRemoteDataSource {
    override suspend fun getAuthUser(): NetworkResult<UserDto> =
        authenticatedClient.safeRequest { get(Endpoints.AUTH_USER_URL) }

    override suspend fun invalidateAuthTokens() {
        authenticatedClient.invalidateAuthTokens()
    }

    override suspend fun deleteUser(userId: Int): NetworkResult<UserDto> =
        authenticatedClient.safeRequest {
            delete("${Endpoints.USERS_URL}/$userId")
        }
}

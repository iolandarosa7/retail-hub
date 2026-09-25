/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.user.data.remote

import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.core.network.endpoint.Endpoints
import com.iolandarosa.retailhub.core.network.extensions.safeRequest
import com.iolandarosa.retailhub.core.user.data.model.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get

internal class UserRemoteDataSourceImpl(
    private val authenticatedClient: HttpClient,
) : UserRemoteDataSource {
    override suspend fun getAuthUser(): NetworkResult<UserDto> =
        authenticatedClient.safeRequest { get(Endpoints.AUTH_USER_URL) }
}

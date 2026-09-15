/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.data.repository

import com.iolandarosa.retailhub.core.datastore.domain.TokenManager
import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.features.profile.data.mapper.toDomain
import com.iolandarosa.retailhub.features.profile.data.remote.ProfileRemoteDataSource
import com.iolandarosa.retailhub.features.profile.domain.model.User
import com.iolandarosa.retailhub.features.profile.domain.repository.ProfileRepository

internal class ProfileRepositoryImpl(
    private val service: ProfileRemoteDataSource,
    private val tokenManager: TokenManager,
) : ProfileRepository {
    override suspend fun getAuthUser(): NetworkResult<User> = service.getAuthUser().map { it.toDomain() }

    override suspend fun logout() {
        tokenManager.clearTokens()
        service.invalidateAuthTokens()
    }
}

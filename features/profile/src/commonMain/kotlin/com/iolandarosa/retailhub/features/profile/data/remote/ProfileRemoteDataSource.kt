/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.data.remote

import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.features.profile.data.model.UserDto

interface ProfileRemoteDataSource {
    suspend fun getAuthUser(): NetworkResult<UserDto>

    suspend fun invalidateAuthTokens()

    suspend fun deleteUser(userId: Int): NetworkResult<UserDto>
}

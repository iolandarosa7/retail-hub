/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.data.remote

import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.core.user.data.model.UserDto

interface ProfileRemoteDataSource {
    suspend fun invalidateAuthTokens()

    suspend fun deleteUser(userId: Int): NetworkResult<UserDto>
}

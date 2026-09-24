/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.user.data.remote

import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.core.user.data.model.UserDto

interface UserRemoteDataSource {
    suspend fun getAuthUser(): NetworkResult<UserDto>
}

/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.domain.repository

import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.features.auth.domain.model.User

interface AuthenticationRepository {
    suspend fun login(
        username: String,
        password: String,
    ): NetworkResult<Unit>

    suspend fun getAuthUser(): NetworkResult<User>
}

/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.domain.repository

import com.iolandarosa.retailhub.core.model.NetworkResult

interface AuthenticationRepository {
    suspend fun login(
        username: String,
        password: String,
    ): NetworkResult<Unit>
}

/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.user.domain.repository

import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.core.user.domain.model.User

interface UserRepository {
    suspend fun getAuthUser(): NetworkResult<User>

    suspend fun getLocalUserImage(userId: Int): ByteArray?
}

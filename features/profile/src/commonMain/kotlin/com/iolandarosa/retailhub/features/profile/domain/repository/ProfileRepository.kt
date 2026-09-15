/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.domain.repository

import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.features.profile.domain.model.User

interface ProfileRepository {
    suspend fun getAuthUser(): NetworkResult<User>

    suspend fun logout()
}

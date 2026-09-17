/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.domain.repository

import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.core.storage.domain.model.ImageStorageResult
import com.iolandarosa.retailhub.features.profile.domain.model.User

interface ProfileRepository {
    suspend fun getAuthUser(): NetworkResult<User>

    suspend fun getLocalUserImage(userId: Int): ByteArray?

    suspend fun saveUserImage(
        userId: Int,
        byteArray: ByteArray,
    ): ImageStorageResult

    suspend fun deleteUserImage(userId: Int): ImageStorageResult

    suspend fun logout()

    suspend fun deleteUser(userId: Int): Boolean
}

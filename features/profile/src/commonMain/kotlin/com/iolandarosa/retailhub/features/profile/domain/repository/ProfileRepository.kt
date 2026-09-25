/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.domain.repository

import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.core.storage.domain.model.ImageStorageResult

interface ProfileRepository {
    suspend fun saveUserImage(
        userId: Int,
        byteArray: ByteArray,
    ): ImageStorageResult

    suspend fun deleteUserImage(userId: Int): ImageStorageResult

    suspend fun logout()

    suspend fun deleteUser(userId: Int): Boolean

    suspend fun login(
        username: String,
        password: String,
    ): NetworkResult<Unit>
}

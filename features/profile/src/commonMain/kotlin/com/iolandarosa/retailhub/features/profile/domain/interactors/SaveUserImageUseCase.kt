/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.domain.interactors

import com.iolandarosa.retailhub.core.storage.domain.model.ImageStorageResult
import com.iolandarosa.retailhub.features.profile.domain.repository.ProfileRepository

interface SaveUserImageUseCase {
    suspend operator fun invoke(
        userId: Int,
        byteArray: ByteArray,
    ): ImageStorageResult
}

internal class SaveUserImageUseCaseImpl(
    private val repository: ProfileRepository,
) : SaveUserImageUseCase {
    override suspend fun invoke(
        userId: Int,
        byteArray: ByteArray,
    ): ImageStorageResult = repository.saveUserImage(userId, byteArray)
}

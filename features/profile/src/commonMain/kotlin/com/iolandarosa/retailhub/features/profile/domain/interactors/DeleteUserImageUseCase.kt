/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.domain.interactors

import com.iolandarosa.retailhub.core.storage.domain.model.ImageStorageResult
import com.iolandarosa.retailhub.features.profile.domain.repository.ProfileRepository

interface DeleteUserImageUseCase {
    suspend operator fun invoke(userId: Int): ImageStorageResult
}

internal class DeleteUserImageUseCaseImpl(
    private val repository: ProfileRepository,
) : DeleteUserImageUseCase {
    override suspend fun invoke(userId: Int): ImageStorageResult = repository.deleteUserImage(userId)
}

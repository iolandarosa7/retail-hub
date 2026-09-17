/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.domain.interactors

import com.iolandarosa.retailhub.features.profile.domain.repository.ProfileRepository

interface DeleteUserUseCase {
    suspend operator fun invoke(userId: Int): Boolean
}

internal class DeleteUserUseCaseImpl(
    private val repository: ProfileRepository,
) : DeleteUserUseCase {
    override suspend fun invoke(userId: Int): Boolean = repository.deleteUser(userId)
}

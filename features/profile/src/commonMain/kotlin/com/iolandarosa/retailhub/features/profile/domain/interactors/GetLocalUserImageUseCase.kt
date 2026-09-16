/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.domain.interactors

import com.iolandarosa.retailhub.features.profile.domain.repository.ProfileRepository

interface GetLocalUserImageUseCase {
    suspend operator fun invoke(userId: Int): ByteArray?
}

internal class GetLocalUserImageUseCaseImpl(
    private val repository: ProfileRepository,
) : GetLocalUserImageUseCase {
    override suspend fun invoke(userId: Int): ByteArray? = repository.getLocalUserImage(userId)
}

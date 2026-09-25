/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.user.domain.interactors

import com.iolandarosa.retailhub.core.user.domain.repository.UserRepository

interface GetLocalUserImageUseCase {
    suspend operator fun invoke(userId: Int): ByteArray?
}

internal class GetLocalUserImageUseCaseImpl(
    private val repository: UserRepository,
) : GetLocalUserImageUseCase {
    override suspend fun invoke(userId: Int): ByteArray? = repository.getLocalUserImage(userId)
}

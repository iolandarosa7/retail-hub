/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.domain.interactors

import com.iolandarosa.retailhub.features.profile.domain.repository.ProfileRepository

interface LogoutUseCase {
    suspend operator fun invoke()
}

internal class LogoutUseCaseImpl(
    private val repository: ProfileRepository,
) : LogoutUseCase {
    override suspend operator fun invoke() = repository.logout()
}

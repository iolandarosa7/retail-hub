/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.domain.interactors

import com.iolandarosa.retailhub.features.auth.domain.repository.AuthenticationRepository

interface LogoutUseCase {
    suspend operator fun invoke()
}

class LogoutUseCaseImpl(
    private val repository: AuthenticationRepository,
) : LogoutUseCase {
    override suspend operator fun invoke() = repository.logout()
}

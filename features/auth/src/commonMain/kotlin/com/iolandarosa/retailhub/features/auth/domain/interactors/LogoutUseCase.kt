/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.domain.interactors

import com.iolandarosa.retailhub.core.datastore.domain.TokenManager

interface LogoutUseCase {
    suspend operator fun invoke()
}

class LogoutUseCaseImpl(
    private val tokenManager: TokenManager,
) : LogoutUseCase {
    override suspend operator fun invoke() = tokenManager.clearTokens()
}

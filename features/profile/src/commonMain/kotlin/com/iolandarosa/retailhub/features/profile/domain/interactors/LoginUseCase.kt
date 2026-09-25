/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.domain.interactors

import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.features.profile.domain.repository.ProfileRepository

interface LoginUseCase {
    suspend operator fun invoke(
        username: String,
        password: String,
    ): NetworkResult<Unit>
}

internal class LoginUseCaseImpl(
    private val repository: ProfileRepository,
) : LoginUseCase {
    override suspend operator fun invoke(
        username: String,
        password: String,
    ) = repository.login(username, password)
}

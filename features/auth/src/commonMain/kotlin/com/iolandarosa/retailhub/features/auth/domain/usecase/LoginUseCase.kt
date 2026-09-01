/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.domain.usecase

import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.features.auth.domain.model.User
import com.iolandarosa.retailhub.features.auth.domain.repository.AuthenticationRepository

interface LoginUseCase {
    suspend operator fun invoke(
        username: String,
        password: String,
    ): NetworkResult<User>
}

class LoginUseCaseImpl(
    private val repository: AuthenticationRepository,
) : LoginUseCase {
    override suspend operator fun invoke(
        username: String,
        password: String,
    ) = repository.login(username, password)
}

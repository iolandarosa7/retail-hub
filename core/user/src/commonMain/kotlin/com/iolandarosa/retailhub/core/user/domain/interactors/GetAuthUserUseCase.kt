/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.user.domain.interactors

import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.core.user.domain.model.User
import com.iolandarosa.retailhub.core.user.domain.repository.UserRepository

interface GetAuthUserUseCase {
    suspend operator fun invoke(): NetworkResult<User>
}

internal class GetAuthUserUseCaseImpl(
    private val repository: UserRepository,
) : GetAuthUserUseCase {
    override suspend operator fun invoke() = repository.getAuthUser()
}

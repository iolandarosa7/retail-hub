package com.iolandarosa.retailhub.features.auth.domain.interactors

import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.features.auth.domain.model.User
import com.iolandarosa.retailhub.features.auth.domain.repository.AuthenticationRepository

interface GetAuthUserUseCase {
    suspend operator fun invoke(): NetworkResult<User>
}

class GetAuthUserUseCaseImpl(
    private val repository: AuthenticationRepository,
) : GetAuthUserUseCase {
    override suspend operator fun invoke() = repository.getAuthUser()
}

/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.domain.interactors

import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.features.profile.domain.model.User
import com.iolandarosa.retailhub.features.profile.domain.repository.ProfileRepository

interface GetAuthUserUseCase {
    suspend operator fun invoke(): NetworkResult<User>
}

class GetAuthUserUseCaseImpl(
    private val repository: ProfileRepository,
) : GetAuthUserUseCase {
    override suspend operator fun invoke() = repository.getAuthUser()
}

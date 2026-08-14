package com.iolandarosa.retailhub.features.auth.domain.repository

import com.iolandarosa.retailhub.core.models.NetworkResult
import com.iolandarosa.retailhub.features.auth.domain.models.User

interface AuthenticationRepository {
    suspend fun login(username: String, password: String): NetworkResult<User>
}
package com.iolandarosa.retailhub.features.auth.data.remote

import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.features.auth.data.model.UserDto
import com.iolandarosa.retailhub.features.auth.data.request.LoginRequest

interface AuthRemoteDataSource {
    suspend fun login(request: LoginRequest): NetworkResult<UserDto>
}
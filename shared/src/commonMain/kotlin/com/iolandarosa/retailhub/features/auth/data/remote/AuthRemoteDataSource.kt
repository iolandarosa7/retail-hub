package com.iolandarosa.retailhub.features.auth.data.remote

import com.iolandarosa.retailhub.core.common.model.NetworkResult
import com.iolandarosa.retailhub.features.auth.data.response.UserDto
import com.iolandarosa.retailhub.features.auth.data.request.LoginRequest

interface AuthRemoteDataSource {
    suspend fun login(request: LoginRequest): NetworkResult<UserDto>
}
package com.iolandarosa.retailhub.features.auth.data.remote

import com.iolandarosa.retailhub.core.models.NetworkResult
import com.iolandarosa.retailhub.features.auth.data.responses.UserDTO
import com.iolandarosa.retailhub.features.auth.data.requests.LoginRequest

interface AuthenticationService {
    suspend fun login(request: LoginRequest): NetworkResult<UserDTO>
}
package com.iolandarosa.retailhub.features.auth.domain.usecase

import com.iolandarosa.retailhub.features.auth.domain.repository.AuthenticationRepository

class LoginUseCase(private val repository: AuthenticationRepository) {
    suspend operator fun invoke(username: String, password: String) =
        repository.login(username, password)
}
package com.iolandarosa.retailhub.features.auth.data.repository

import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.features.auth.data.mapper.toDomain
import com.iolandarosa.retailhub.features.auth.data.model.UserDto
import com.iolandarosa.retailhub.features.auth.data.remote.AuthRemoteDataSource
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthenticationRepositoryImplTest {
    private val service = mock<AuthRemoteDataSource>()
    private val repository = AuthenticationRepositoryImpl(service)

    @Test
    fun loginReturnsMappedUserWhenServiceSucceeds() = runTest {
        val userDto = UserDto(
            id = 1,
            username = "username",
            email = "email",
            firstName = "firstName",
            lastName = "lastName",
            gender = "gender",
            image = "image",
            accessToken = "accessToken",
            refreshToken = "refreshToken"
        )

        everySuspend { service.login(any())} returns NetworkResult.Success(data = userDto)

        val result = repository.login(username = "john", password = "password")

        assertEquals(NetworkResult.Success(userDto.toDomain()), result)

        verifySuspend { service.login(any()) }
    }

    @Test
    fun loginReturnsErrorWhenServiceFails() = runTest {
        everySuspend { service.login(any())} returns NetworkResult.Failure.Timeout

        val result = repository.login(username = "john", password = "password")

        assertEquals(NetworkResult.Failure.Timeout, result)

        verifySuspend { service.login(any()) }
    }
}
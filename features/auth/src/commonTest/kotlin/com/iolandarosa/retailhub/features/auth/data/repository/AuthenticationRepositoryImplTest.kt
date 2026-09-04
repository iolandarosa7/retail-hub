/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.data.repository

import com.iolandarosa.retailhub.core.datastore.domain.TokenManager
import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.features.auth.data.mapper.toDomain
import com.iolandarosa.retailhub.features.auth.data.model.AddressDto
import com.iolandarosa.retailhub.features.auth.data.model.AuthenticationDto
import com.iolandarosa.retailhub.features.auth.data.model.BankDto
import com.iolandarosa.retailhub.features.auth.data.model.CompanyDto
import com.iolandarosa.retailhub.features.auth.data.model.CoordinatesDto
import com.iolandarosa.retailhub.features.auth.data.model.CryptoDto
import com.iolandarosa.retailhub.features.auth.data.model.HairDto
import com.iolandarosa.retailhub.features.auth.data.model.UserDto
import com.iolandarosa.retailhub.features.auth.data.remote.AuthRemoteDataSource
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthenticationRepositoryImplTest {
    private val service = mock<AuthRemoteDataSource>()
    private val tokenManager = mock<TokenManager>()
    private val repository = AuthenticationRepositoryImpl(service, tokenManager)

    @Test
    fun success_login_callsSaveTokensAndHasUnitResponse() =
        runTest {
            val authenticationDto =
                AuthenticationDto(
                    id = 1,
                    username = "username",
                    email = "email",
                    firstName = "firstName",
                    lastName = "lastName",
                    gender = "gender",
                    image = "image",
                    accessToken = "accessToken",
                    refreshToken = "refreshToken",
                )

            everySuspend { service.login(any()) } returns NetworkResult.Success(data = authenticationDto)
            everySuspend { tokenManager.saveAuthTokens(any(), any()) } returns Unit

            val result = repository.login(username = "john", password = "password")

            assertEquals(NetworkResult.Success(Unit), result)

            verifySuspend { service.login(any()) }
            verifySuspend { tokenManager.saveAuthTokens(authenticationDto.accessToken, authenticationDto.refreshToken) }
        }

    @Test
    fun error_login_notCallSaveTokensAndHasErrorResponse() =
        runTest {
            everySuspend { service.login(any()) } returns NetworkResult.Failure.Timeout

            val result = repository.login(username = "john", password = "password")

            assertEquals(NetworkResult.Failure.Timeout, result)

            verifySuspend { service.login(any()) }
            verifySuspend(VerifyMode.not) { tokenManager.saveAuthTokens(any(), any()) }
        }

    @Test
    fun success_getAuthUser_returnsMappedResponse() =
        runTest {
            val userDto =
                UserDto(
                    id = 1,
                    firstName = "John",
                    lastName = "Doe",
                    maidenName = "",
                    age = 30,
                    gender = "male",
                    email = "john@example.com",
                    phone = "123456",
                    username = "johndoe",
                    password = "password",
                    birthDate = "2000-01-01",
                    image = "image",
                    bloodGroup = "A+",
                    height = 180.0,
                    weight = 80.0,
                    eyeColor = "brown",
                    hair = HairDto("", ""),
                    ip = "",
                    address = AddressDto("", "", "", "", "", CoordinatesDto(0.0, 0.0), ""),
                    macAddress = "",
                    university = "",
                    bank = BankDto("", "", "", "", ""),
                    company = CompanyDto("", "", "", AddressDto("", "", "", "", "", CoordinatesDto(0.0, 0.0), "")),
                    ein = "",
                    ssn = "",
                    userAgent = "",
                    crypto = CryptoDto("", "", ""),
                    role = "admin",
                )

            everySuspend { service.getAuthUser() } returns NetworkResult.Success(userDto)

            val result = repository.getAuthUser()

            assertEquals(NetworkResult.Success(userDto.toDomain()), result)

            verifySuspend { service.getAuthUser() }
        }

    @Test
    fun error_getAuthUser_returnsErrorResponse() =
        runTest {
            everySuspend { service.getAuthUser() } returns NetworkResult.Failure.Unauthorized

            val result = repository.getAuthUser()

            assertEquals(NetworkResult.Failure.Unauthorized, result)

            verifySuspend { service.getAuthUser() }
        }
}

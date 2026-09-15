/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.data.repository

import com.iolandarosa.retailhub.core.datastore.domain.TokenManager
import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.features.profile.data.mapper.toDomain
import com.iolandarosa.retailhub.features.profile.data.model.AddressDto
import com.iolandarosa.retailhub.features.profile.data.model.BankDto
import com.iolandarosa.retailhub.features.profile.data.model.CompanyDto
import com.iolandarosa.retailhub.features.profile.data.model.CoordinatesDto
import com.iolandarosa.retailhub.features.profile.data.model.CryptoDto
import com.iolandarosa.retailhub.features.profile.data.model.HairDto
import com.iolandarosa.retailhub.features.profile.data.model.UserDto
import com.iolandarosa.retailhub.features.profile.data.remote.ProfileRemoteDataSource
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ProfileRepositoryImplTest {
    private val service = mock<ProfileRemoteDataSource>()
    private val tokenManager = mock<TokenManager>()
    private val repository = ProfileRepositoryImpl(service, tokenManager)

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
                    address = AddressDto("", "", "", "", "", CoordinatesDto(lat = 0.0, lng = 0.0), ""),
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

    @Test
    fun initialState_logout_callsExpectedMethods() =
        runTest {
            everySuspend { tokenManager.clearTokens() } returns Unit
            everySuspend { service.invalidateAuthTokens() } returns Unit

            repository.logout()

            verifySuspend { service.invalidateAuthTokens() }
            verifySuspend { tokenManager.clearTokens() }
        }
}

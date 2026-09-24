/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.user.data.repository

import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.core.storage.domain.LocalImageStorage
import com.iolandarosa.retailhub.core.user.data.mapper.toDomain
import com.iolandarosa.retailhub.core.user.data.model.AddressDto
import com.iolandarosa.retailhub.core.user.data.model.BankDto
import com.iolandarosa.retailhub.core.user.data.model.CompanyDto
import com.iolandarosa.retailhub.core.user.data.model.CoordinatesDto
import com.iolandarosa.retailhub.core.user.data.model.CryptoDto
import com.iolandarosa.retailhub.core.user.data.model.HairDto
import com.iolandarosa.retailhub.core.user.data.model.UserDto
import com.iolandarosa.retailhub.core.user.data.remote.UserRemoteDataSource
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class UserRepositoryImplTest {
    private val service = mock<UserRemoteDataSource>()
    private val localImageStorage = mock<LocalImageStorage>()
    private val repository = UserRepositoryImpl(service, localImageStorage)

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
    fun getLocalUserImage_callsStorageLoad() =
        runTest {
            val userId = 1
            val expectedBytes = byteArrayOf(1, 2, 3)
            everySuspend { localImageStorage.load("$userId") } returns expectedBytes

            val result = repository.getLocalUserImage(userId)

            assertEquals(expectedBytes, result)
            verifySuspend { localImageStorage.load("$userId") }
        }
}

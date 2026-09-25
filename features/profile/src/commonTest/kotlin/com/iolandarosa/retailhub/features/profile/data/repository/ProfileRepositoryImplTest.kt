/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.data.repository

import com.iolandarosa.retailhub.core.datastore.domain.TokenManager
import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.core.storage.domain.LocalImageStorage
import com.iolandarosa.retailhub.core.storage.domain.model.ImageStorageResult
import com.iolandarosa.retailhub.core.user.data.model.AddressDto
import com.iolandarosa.retailhub.core.user.data.model.BankDto
import com.iolandarosa.retailhub.core.user.data.model.CompanyDto
import com.iolandarosa.retailhub.core.user.data.model.CoordinatesDto
import com.iolandarosa.retailhub.core.user.data.model.CryptoDto
import com.iolandarosa.retailhub.core.user.data.model.HairDto
import com.iolandarosa.retailhub.core.user.data.model.UserDto
import com.iolandarosa.retailhub.features.profile.data.model.AuthenticationDto
import com.iolandarosa.retailhub.features.profile.data.remote.ProfileRemoteDataSource
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ProfileRepositoryImplTest {
    private val service = mock<ProfileRemoteDataSource>()
    private val tokenManager = mock<TokenManager>()

    private val localImageStorage = mock<LocalImageStorage>()
    private val repository = ProfileRepositoryImpl(service, tokenManager, localImageStorage)

    @Test
    fun initialState_logout_callsExpectedMethods() =
        runTest {
            everySuspend { tokenManager.clearTokens() } returns Unit
            everySuspend { service.invalidateAuthTokens() } returns Unit

            repository.logout()

            verifySuspend { service.invalidateAuthTokens() }
            verifySuspend { tokenManager.clearTokens() }
        }

    @Test
    fun saveUserImage_callsStorageSave() =
        runTest {
            val userId = 1
            val bytes = byteArrayOf(4, 5, 6)
            everySuspend { localImageStorage.save("$userId", bytes) } returns ImageStorageResult.Success

            val result = repository.saveUserImage(userId, bytes)

            assertEquals(ImageStorageResult.Success, result)
            verifySuspend { localImageStorage.save("$userId", bytes) }
        }

    @Test
    fun deleteUserImage_callsStorageDelete() =
        runTest {
            val userId = 1
            everySuspend { localImageStorage.delete("$userId") } returns ImageStorageResult.Success

            val result = repository.deleteUserImage(userId)

            assertEquals(ImageStorageResult.Success, result)
            verifySuspend { localImageStorage.delete("$userId") }
        }

    @Test
    fun deleteUser_success_clearsTokensAndReturnsTrue() =
        runTest {
            val userId = 1
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
                    isDeleted = true,
                )

            everySuspend { service.deleteUser(userId) } returns NetworkResult.Success(userDto)
            everySuspend { localImageStorage.delete("$userId") } returns ImageStorageResult.Success
            everySuspend { tokenManager.clearTokens() } returns Unit
            everySuspend { service.invalidateAuthTokens() } returns Unit

            val result = repository.deleteUser(userId)

            assertEquals(true, result)
            verifySuspend { service.deleteUser(userId) }
            verifySuspend { localImageStorage.delete("$userId") }
            verifySuspend { tokenManager.clearTokens() }
            verifySuspend { service.invalidateAuthTokens() }
        }

    @Test
    fun deleteUser_notDeleted_returnsFalse() =
        runTest {
            val userId = 1
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
                    isDeleted = false,
                )

            everySuspend { service.deleteUser(userId) } returns NetworkResult.Success(userDto)

            val result = repository.deleteUser(userId)

            assertEquals(false, result)
            verifySuspend { service.deleteUser(userId) }
        }

    @Test
    fun deleteUser_failure_returnsFalse() =
        runTest {
            val userId = 1
            everySuspend { service.deleteUser(userId) } returns NetworkResult.Failure.Unknown()

            val result = repository.deleteUser(userId)

            assertEquals(false, result)
            verifySuspend { service.deleteUser(userId) }
        }

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
}

/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.data.remote

import com.iolandarosa.retailhub.core.datastore.domain.TokenManager
import com.iolandarosa.retailhub.core.model.AuthTokens
import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.core.network.client.createAuthenticatedClient
import com.iolandarosa.retailhub.core.network.client.createPublicClient
import com.iolandarosa.retailhub.core.network.endpoint.Endpoints
import com.iolandarosa.retailhub.features.profile.data.model.AddressDto
import com.iolandarosa.retailhub.features.profile.data.model.BankDto
import com.iolandarosa.retailhub.features.profile.data.model.CompanyDto
import com.iolandarosa.retailhub.features.profile.data.model.CoordinatesDto
import com.iolandarosa.retailhub.features.profile.data.model.CryptoDto
import com.iolandarosa.retailhub.features.profile.data.model.HairDto
import com.iolandarosa.retailhub.features.profile.data.model.UserDto
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ProfileRemoteDataSourceImplTest {
    private val tokenManager: TokenManager = mock()

    @Test
    fun success_getAuthUser_hasExpectedResult() =
        runTest {
            everySuspend { tokenManager.getAuthTokens() } returns flowOf(AuthTokens("access", "refresh"))

            val responseJson =
                """
                {
                    "id": 1,
                    "firstName": "John",
                    "lastName": "Doe",
                    "maidenName": "",
                    "age": 30,
                    "gender": "male",
                    "email": "john@example.com",
                    "phone": "123456",
                    "username": "johndoe",
                    "password": "password",
                    "birthDate": "2000-01-01",
                    "image": "image",
                    "bloodGroup": "A+",
                    "height": 180.0,
                    "weight": 80.0,
                    "eyeColor": "brown",
                    "hair": { "color": "", "type": "" },
                    "ip": "",
                    "address": { "address": "", "city": "", "state": "", "stateCode": "", "postalCode": "", "coordinates": { "lat": 0.0, "lng": 0.0 }, "country": "" },
                    "macAddress": "",
                    "university": "",
                    "bank": { "cardExpire": "", "cardNumber": "", "cardType": "", "currency": "", "iban": "" },
                    "company": { "department": "", "name": "", "title": "", "address": { "address": "", "city": "", "state": "", "stateCode": "", "postalCode": "", "coordinates": { "lat": 0.0, "lng": 0.0 }, "country": "" } },
                    "ein": "",
                    "ssn": "",
                    "userAgent": "",
                    "crypto": { "coin": "", "wallet": "", "network": "" },
                    "role": "admin"
                }
                """.trimIndent()

            val engine =
                MockEngine { request ->
                    assertEquals(HttpMethod.Get, request.method)
                    assertEquals(Endpoints.AUTH_USER_URL, request.url.encodedPath)

                    respond(
                        content = responseJson,
                        status = HttpStatusCode.OK,
                        headers =
                            headersOf(
                                HttpHeaders.ContentType,
                                ContentType.Application.Json.toString(),
                            ),
                    )
                }

            val publicClient = createPublicClient(engine)
            val authenticatedClient = createAuthenticatedClient(tokenManager, publicClient, engine)
            val dataSource = ProfileRemoteDataSourceImpl(authenticatedClient)

            val result = dataSource.getAuthUser()

            val expectedUserDto =
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

            assertEquals(NetworkResult.Success(expectedUserDto), result)
        }

    @Test
    fun error_getAuthUser_hasExpectedResult() =
        runTest {
            val engine =
                MockEngine {
                    respond(
                        content = "",
                        status = HttpStatusCode.Unauthorized,
                    )
                }

            val publicClient = createPublicClient(engine)
            val authenticatedClient = createAuthenticatedClient(tokenManager, publicClient, engine)
            val dataSource = ProfileRemoteDataSourceImpl(authenticatedClient)

            val result = dataSource.getAuthUser()

            assertIs<NetworkResult.Failure.Unknown>(result)
        }

    @Test
    fun decodeError_getAuthUser_hasExpectedResponse() =
        runTest {
            val engine =
                MockEngine {
                    respond(
                        content = "{ invalid }",
                        status = HttpStatusCode.OK,
                        headers =
                            headersOf(
                                HttpHeaders.ContentType,
                                ContentType.Application.Json.toString(),
                            ),
                    )
                }

            val publicClient = createPublicClient(engine)
            val authenticatedClient = createAuthenticatedClient(tokenManager, publicClient, engine)
            val dataSource = ProfileRemoteDataSourceImpl(authenticatedClient)

            val result = dataSource.getAuthUser()

            assertIs<NetworkResult.Failure.Unknown>(result)
        }

    @Test
    fun success_deleteUser_hasExpectedResult() =
        runTest {
            everySuspend { tokenManager.getAuthTokens() } returns flowOf(AuthTokens("access", "refresh"))

            val responseJson =
                """
                {
                    "id": 1,
                    "firstName": "John",
                    "lastName": "Doe",
                    "maidenName": "",
                    "age": 30,
                    "gender": "male",
                    "email": "john@example.com",
                    "phone": "123456",
                    "username": "johndoe",
                    "password": "password",
                    "birthDate": "2000-01-01",
                    "image": "image",
                    "bloodGroup": "A+",
                    "height": 180.0,
                    "weight": 80.0,
                    "eyeColor": "brown",
                    "hair": { "color": "", "type": "" },
                    "ip": "",
                    "address": { "address": "", "city": "", "state": "", "stateCode": "", "postalCode": "", "coordinates": { "lat": 0.0, "lng": 0.0 }, "country": "" },
                    "macAddress": "",
                    "university": "",
                    "bank": { "cardExpire": "", "cardNumber": "", "cardType": "", "currency": "", "iban": "" },
                    "company": { "department": "", "name": "", "title": "", "address": { "address": "", "city": "", "state": "", "stateCode": "", "postalCode": "", "coordinates": { "lat": 0.0, "lng": 0.0 }, "country": "" } },
                    "ein": "",
                    "ssn": "",
                    "userAgent": "",
                    "crypto": { "coin": "", "wallet": "", "network": "" },
                    "role": "admin",
                    "isDeleted": true
                }
                """.trimIndent()

            val engine =
                MockEngine { request ->
                    assertEquals(HttpMethod.Delete, request.method)
                    assertEquals("/${Endpoints.USERS_URL}/1", request.url.encodedPath)

                    respond(
                        content = responseJson,
                        status = HttpStatusCode.OK,
                        headers =
                            headersOf(
                                HttpHeaders.ContentType,
                                ContentType.Application.Json.toString(),
                            ),
                    )
                }

            val publicClient = createPublicClient(engine)
            val authenticatedClient = createAuthenticatedClient(tokenManager, publicClient, engine)
            val dataSource = ProfileRemoteDataSourceImpl(authenticatedClient)

            val result = dataSource.deleteUser(1)

            val expectedUserDto =
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
                    isDeleted = true,
                )

            assertEquals(NetworkResult.Success(expectedUserDto), result)
        }
}

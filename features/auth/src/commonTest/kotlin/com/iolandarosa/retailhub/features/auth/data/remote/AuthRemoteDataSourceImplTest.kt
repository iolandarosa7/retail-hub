/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.data.remote

import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.core.network.client.createPublicClient
import com.iolandarosa.retailhub.core.network.endpoint.Endpoints
import com.iolandarosa.retailhub.features.auth.data.model.UserDto
import com.iolandarosa.retailhub.features.auth.data.request.LoginRequest
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class AuthRemoteDataSourceImplTest {
    @Test
    fun loginSendsCorrectRequestAndReturnsUser() =
        runTest {
            val responseJson =
                """
                {
                    "id": 1,
                    "username": "john",
                    "email": "john@example.com",
                    "firstName": "firstName",
                    "lastName": "lastName",
                    "gender": "gender",
                    "image": "image",
                    "accessToken": "accessToken",
                    "refreshToken": "refreshToken"
                }
                """.trimIndent()

            val engine =
                MockEngine { request ->

                    assertEquals(
                        HttpMethod.Post,
                        request.method,
                    )

                    assertEquals(
                        Endpoints.LOGIN_URL,
                        request.url.encodedPath,
                    )

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

            val client = createPublicClient(engine)

            val dataSource = AuthRemoteDataSourceImpl(client)

            val result =
                dataSource.login(
                    LoginRequest(
                        username = "john",
                        password = "secret",
                        expiresInMins = 5,
                    ),
                )

            assertEquals(
                NetworkResult.Success(
                    UserDto(
                        id = 1,
                        username = "john",
                        email = "john@example.com",
                        firstName = "firstName",
                        lastName = "lastName",
                        gender = "gender",
                        image = "image",
                        accessToken = "accessToken",
                        refreshToken = "refreshToken",
                    ),
                ),
                result,
            )
        }

    @Test
    fun loginReturnsErrorWhenServerReturnsUnauthorized() =
        runTest {
            val engine =
                MockEngine {
                    respond(
                        content =
                            """
                            {
                                "message": "Invalid credentials"
                            }
                            """.trimIndent(),
                        status = HttpStatusCode.Unauthorized,
                        headers =
                            headersOf(
                                HttpHeaders.ContentType,
                                ContentType.Application.Json.toString(),
                            ),
                    )
                }

            val client = createPublicClient(engine)

            val dataSource = AuthRemoteDataSourceImpl(client)

            val result =
                dataSource.login(
                    LoginRequest(
                        username = "john",
                        password = "wrong",
                        expiresInMins = 5,
                    ),
                )

            assertIs<NetworkResult.Failure.Unauthorized>(result)
        }

    @Test
    fun loginReturnsErrorWhenResponseCannotBeDecoded() =
        runTest {
            val engine =
                MockEngine {
                    respond(
                        content = "this is not valid json",
                        status = HttpStatusCode.OK,
                        headers =
                            headersOf(
                                HttpHeaders.ContentType,
                                ContentType.Application.Json.toString(),
                            ),
                    )
                }

            val client = createPublicClient(engine)

            val dataSource = AuthRemoteDataSourceImpl(client)

            val result =
                dataSource.login(
                    LoginRequest(
                        username = "john",
                        password = "secret",
                        expiresInMins = 5,
                    ),
                )

            assertIs<NetworkResult.Failure.Unknown>(result)
        }
}

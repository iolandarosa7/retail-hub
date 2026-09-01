/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.network.client

import com.iolandarosa.retailhub.core.datastore.domain.TokenManager
import com.iolandarosa.retailhub.core.model.AuthTokens
import com.iolandarosa.retailhub.core.network.endpoint.Endpoints
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.engine.mock.toByteArray
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class NetworkClientFactoryTest {
    private val tokenManager = mock<TokenManager>()

    @Test
    fun createPublicHttpClient_appliesAllRequiredPlugins() =
        runTest {
            val engine =
                MockEngine { _ ->
                    respondOk()
                }

            val client = createPublicClient(engine)

            assertNotNull(client.plugin(ContentNegotiation), "ContentNegotiation should be installed")
            assertNotNull(client.plugin(Logging), "Logging should be installed")
            assertNotNull(client.plugin(DefaultRequest), "DefaultRequest should be installed")
            assertNotNull(client.plugin(HttpTimeout), "HttpTimeout should be installed")
        }

    @Test
    fun createPublicHttpClient_configuresBaseUrlCorrectly() =
        runTest {
            var capturedUrl = ""
            val engine =
                MockEngine { request ->
                    capturedUrl = request.url.toString()
                    respondOk()
                }

            val client = createPublicClient(engine)

            client.get("test")

            assertTrue(
                capturedUrl.startsWith(Endpoints.BASE_URL),
                "URL should start with Base URL: $capturedUrl",
            )
            assertTrue(capturedUrl.endsWith("/test"), "URL should end with /test: $capturedUrl")
        }

    @Test
    fun createAuthenticatedClient_appliesAllRequiredPlugins() =
        runTest {
            val engine =
                MockEngine { _ ->
                    respondOk()
                }

            val publicClient = createPublicClient(engine)

            val client = createAuthenticatedClient(tokenManager, publicClient, engine)

            assertNotNull(client.plugin(ContentNegotiation), "ContentNegotiation should be installed")
            assertNotNull(client.plugin(Logging), "Logging should be installed")
            assertNotNull(client.plugin(DefaultRequest), "DefaultRequest should be installed")
            assertNotNull(client.plugin(HttpTimeout), "HttpTimeout should be installed")
            assertNotNull(client.plugin(Auth), "Auth should be installed")
        }

    @Test
    fun authenticatedClient_loadsTokensFromTokenManager() =
        runTest {
            val accessToken = "accessToken"
            everySuspend { tokenManager.getAuthTokens() } returns
                flowOf(
                    AuthTokens(accessToken, "refreshToken"),
                )

            var authorizationHeader: String? = null

            val engine =
                MockEngine { request ->
                    authorizationHeader = request.headers["Authorization"]
                    respondOk()
                }

            val publicClient = createPublicClient(engine)

            val client = createAuthenticatedClient(tokenManager, publicClient, engine)

            client.get("test")

            assertEquals("Bearer $accessToken", authorizationHeader)
        }

    @Test
    fun authenticatedClient_refreshesTokensAfterUnauthorized() =
        runTest {
            val oldAccessToken = "oldAccessToken"
            val oldRefreshToken = "oldRefreshToken"

            val newAccessToken = "newAccessToken"
            val newRefreshToken = "newRefreshToken"

            everySuspend { tokenManager.getAuthTokens() } returns
                flowOf(
                    AuthTokens(oldAccessToken, oldRefreshToken),
                )

            everySuspend { tokenManager.saveAuthTokens(any(), any()) } returns Unit

            var requestCount = 0
            var refreshRequestBody: String? = null
            val authorizationHeaders = mutableListOf<String?>()

            val engine =
                MockEngine { request ->
                    when {
                        request.url.encodedPath == Endpoints.REFRESH_URL -> {
                            refreshRequestBody = request.body.toByteArray().decodeToString()

                            respond(
                                content =
                                    """
                                    {
                                        "accessToken": "$newAccessToken",
                                        "refreshToken": "$newRefreshToken"
                                    }
                                    """.trimIndent(),
                                status = HttpStatusCode.OK,
                                headers =
                                    headersOf(
                                        HttpHeaders.ContentType,
                                        ContentType.Application.Json.toString(),
                                    ),
                            )
                        }

                        else -> {
                            requestCount++
                            authorizationHeaders += request.headers[HttpHeaders.Authorization]

                            if (requestCount == 1) {
                                respond(
                                    content = "",
                                    status = HttpStatusCode.Unauthorized,
                                )
                            } else {
                                respondOk()
                            }
                        }
                    }
                }

            val publicClient = createPublicClient(engine)

            val client =
                createAuthenticatedClient(
                    tokenManager = tokenManager,
                    publicClient = publicClient,
                    engine = engine,
                )

            client.get("test")

            assertEquals(
                listOf<String?>(
                    "Bearer $oldAccessToken",
                    "Bearer $newAccessToken",
                ),
                authorizationHeaders,
            )

            assertTrue(refreshRequestBody!!.contains(oldRefreshToken))

            verifySuspend { tokenManager.saveAuthTokens(newAccessToken, newRefreshToken) }
        }

    @Test
    fun authenticatedClient_clearsTokensWhenRefreshFails() =
        runTest {
            val oldAccessToken = "oldAccessToken"
            val oldRefreshToken = "oldRefreshToken"

            everySuspend { tokenManager.getAuthTokens() } returns
                flowOf(
                    AuthTokens(oldAccessToken, oldRefreshToken),
                )

            everySuspend { tokenManager.clearTokens() } returns Unit

            val engine =
                MockEngine {
                    respond(
                        content = "",
                        status = HttpStatusCode.Unauthorized,
                    )
                }

            val publicClient = createPublicClient(engine)

            val client = createAuthenticatedClient(tokenManager, publicClient, engine)

            client.get("test")

            verifySuspend(VerifyMode.exactly(1)) { tokenManager.clearTokens() }
        }
}

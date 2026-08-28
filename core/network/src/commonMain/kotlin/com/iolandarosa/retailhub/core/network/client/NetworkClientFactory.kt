package com.iolandarosa.retailhub.core.network.client

import com.iolandarosa.retailhub.core.datastore.domain.TokenManager
import com.iolandarosa.retailhub.core.model.AuthTokens
import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.core.model.RefreshTokenRequest
import com.iolandarosa.retailhub.core.network.endpoint.Endpoints
import com.iolandarosa.retailhub.core.network.extensions.safeRequest
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json

expect fun platformHttpClient(): HttpClient

private val networkJson = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
    isLenient = true
}

private fun HttpClientConfig<*>.commonConfig() {
    install(ContentNegotiation) {
        json(networkJson)
    }

    install(Logging) {
        level = LogLevel.ALL
    }

    install(DefaultRequest) {
        url(Endpoints.BASE_URL)
        contentType(ContentType.Application.Json)
        accept(ContentType.Application.Json)
    }

    install(HttpTimeout) {
        requestTimeoutMillis = 30_000
        connectTimeoutMillis = 30_000
    }
}

fun createPublicClient(engine: HttpClientEngine? = null): HttpClient {
    val client = engine?.let { HttpClient(it) } ?: platformHttpClient()
    return client.config { commonConfig() }
}

fun createAuthenticatedClient(
    tokenManager: TokenManager,
    publicClient: HttpClient,
    engine: HttpClientEngine? = null
): HttpClient {
    val client = engine?.let { HttpClient(it) } ?: platformHttpClient()
    return client.config {
        commonConfig()

        install(Auth) {
            bearer {
                loadTokens {
                    tokenManager.getAuthTokens().firstOrNull()?.let {
                        BearerTokens(it.accessToken, it.refreshToken)
                    }
                }

                refreshTokens {
                    val refreshToken = oldTokens?.refreshToken ?: return@refreshTokens null

                    val response: NetworkResult<AuthTokens> = publicClient.safeRequest {
                        post(Endpoints.REFRESH_URL) {
                            contentType(ContentType.Application.Json)
                            setBody(RefreshTokenRequest(refreshToken, 5))
                        }
                    }

                    when (response) {
                        is NetworkResult.Success -> {
                            tokenManager.saveAuthTokens(
                                accessToken = response.data.accessToken,
                                refreshToken = response.data.refreshToken
                            )

                            BearerTokens(
                                accessToken = response.data.accessToken,
                                refreshToken = response.data.refreshToken
                            )
                        }

                        is NetworkResult.Failure.Unauthorized -> {
                            tokenManager.clearTokens()
                            null
                        }

                        else -> null
                    }
                }
            }
        }
    }
}

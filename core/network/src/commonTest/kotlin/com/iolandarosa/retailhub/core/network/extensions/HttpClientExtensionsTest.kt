package com.iolandarosa.retailhub.core.network.extensions

import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.core.network.TestDto
import com.iolandarosa.retailhub.core.network.client.createHttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class HttpClientExtensionsTest {

    @Test
    fun successfulRequest_safeRequest_returnsSuccess() = runTest {
        val client = createHttpClient(MockEngine {
            respond(
                content = """{"id": 1, "name": "Test"}""",
                status = HttpStatusCode.OK,
                headers = headersOf(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json.toString(),
                ),
            )
        })

        val result = client.safeRequest<TestDto> { get("/") }

        assertIs<NetworkResult.Success<TestDto>>(result)
        assertEquals(TestDto(1, "Test"), result.data)
    }

    @Test
    fun unresolvedAddressException_safeRequest_returnsNoInternet() = runTest {
        val client = createHttpClient(MockEngine {
            throw UnresolvedAddressException()
        })

        val result = client.safeRequest<TestDto> { get("/") }

        assertIs<NetworkResult.Failure.NoInternet>(result)
    }

    @Test
    fun connectTimeoutException_safeRequest_returnsTimeout() = runTest {
        val client = createHttpClient(MockEngine {
            throw ConnectTimeoutException("host", null)
        })

        val result = client.safeRequest<TestDto> { get("/") }

        assertIs<NetworkResult.Failure.Timeout>(result)
    }

    @Test
    fun httpRequestTimeoutException_safeRequest_returnsTimeout() = runTest {
        val client = createHttpClient(MockEngine {
            throw HttpRequestTimeoutException("url", 1000L)
        })

        val result = client.safeRequest<TestDto> { get("/") }

        assertIs<NetworkResult.Failure.Timeout>(result)
    }

    @Test
    fun socketTimeoutException_safeRequest_returnsTimeout() = runTest {
        val client = createHttpClient(MockEngine {
            throw SocketTimeoutException("timeout")
        })

        val result = client.safeRequest<TestDto> { get("/") }

        assertIs<NetworkResult.Failure.Timeout>(result)
    }

    @Test
    fun serializationException_safeRequest_returnsSerializationFailure() = runTest {
        val client = createHttpClient(MockEngine {
            throw SerializationException("serialization error")
        })

        val result = client.safeRequest<TestDto> { get("/") }

        assertIs<NetworkResult.Failure.Serialization>(result)
        assertEquals("serialization error", result.message)
    }

    @Test
    fun genericException_safeRequest_returnsUnknownFailure() = runTest {
        val errorMessage = "Something went wrong"
        val client = createHttpClient(MockEngine {
            throw Exception(errorMessage)
        })

        val result = client.safeRequest<TestDto> { get("/") }

        assertIs<NetworkResult.Failure.Unknown>(result)
        assertEquals(errorMessage, result.message)
    }
}

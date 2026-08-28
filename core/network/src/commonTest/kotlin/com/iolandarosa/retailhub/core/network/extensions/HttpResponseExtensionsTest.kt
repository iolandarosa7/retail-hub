package com.iolandarosa.retailhub.core.network.extensions

import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.core.network.TestDto
import com.iolandarosa.retailhub.core.network.client.createPublicClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class HttpResponseExtensionsTest {

    @Test
    fun statusSuccessValidSerialization_handleResponse_expectsSuccessResult() = runTest {
        val client = createPublicClient(MockEngine {
            respond(
                content = """{"id": 1, "name": "Test"}""",
                status = HttpStatusCode.OK,
                headers = headersOf(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json.toString(),
                ),
            )
        })

        val response = client.get("/")
        val result = response.handleResponse<TestDto>()

        assertIs<NetworkResult.Success<TestDto>>(result)
        assertEquals(TestDto(1, "Test"), result.data)
    }

    @Test
    fun statusSuccessInvalidSerialization_handleResponse_expectsSerializationError() = runTest {
        val client = createPublicClient(MockEngine {
            respond(
                content = """{"invalid": "json"}""",
                status = HttpStatusCode.OK,
                headers = headersOf(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json.toString(),
                ),
            )
        })

        val response = client.get("/")

        val result = response.handleResponse<TestDto>()

        assertIs<NetworkResult.Failure.Unknown>(result)
    }

    @Test
    fun statusSuccessUnitResponse_handleResponse_expectsSuccessUnit() = runTest {
        val client = createPublicClient(MockEngine {
            respond(
                content = "",
                status = HttpStatusCode.NoContent,
            )
        })

        val response = client.get("/")
        val result = response.handleResponse<Unit>()

        assertIs<NetworkResult.Success<Unit>>(result)
        assertEquals(Unit, result.data)
    }

    @Test
    fun statusUnauthorized_handleResponse_expectsUnauthorizedFailure() = runTest {
        val client = createPublicClient(MockEngine {
            respond(
                content = "",
                status = HttpStatusCode.Unauthorized,
            )
        })

        val response = client.get("/")
        val result = response.handleResponse<TestDto>()

        assertIs<NetworkResult.Failure.Unauthorized>(result)
    }

    @Test
    fun statusForbidden_handleResponse_expectsForbiddenFailure() = runTest {
        val client = createPublicClient(MockEngine {
            respond(
                content = "",
                status = HttpStatusCode.Forbidden,
            )
        })

        val response = client.get("/")
        val result = response.handleResponse<TestDto>()

        assertIs<NetworkResult.Failure.Forbidden>(result)
    }

    @Test
    fun statusServerError_handleResponse_expectsServerFailure() = runTest {
        val errorMessage = "Internal Server Error"
        val client = createPublicClient(MockEngine {
            respond(
                content = errorMessage,
                status = HttpStatusCode.InternalServerError,
            )
        })

        val response = client.get("/")
        val result = response.handleResponse<TestDto>()

        assertIs<NetworkResult.Failure.Server>(result)
        assertEquals(HttpStatusCode.InternalServerError.value, result.code)
        assertEquals(errorMessage, result.message)
    }

    @Test
    fun statusBadRequestWithApiError_handleResponse_expectsApiErrorFailure() = runTest {
        val errorMessage = "Bad Request"
        val client = createPublicClient(MockEngine {
            respond(
                content = """{"message": "$errorMessage"}""",
                status = HttpStatusCode.BadRequest,
                headers = headersOf(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json.toString(),
                ),
            )
        })

        val response = client.get("/")
        val result = response.handleResponse<TestDto>()

        assertIs<NetworkResult.Failure.ApiError>(result)
        assertEquals(errorMessage, result.error.message)
    }
}

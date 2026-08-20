package com.iolandarosa.retailhub.core.ui.extension

import com.iolandarosa.retailhub.core.model.ApiErrorResponse
import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.core.ui.error.UiError
import retailhub.core.ui.generated.resources.Res
import retailhub.core.ui.generated.resources.error_forbidden
import retailhub.core.ui.generated.resources.error_no_internet
import retailhub.core.ui.generated.resources.error_serialization
import retailhub.core.ui.generated.resources.error_server
import retailhub.core.ui.generated.resources.error_timeout
import retailhub.core.ui.generated.resources.error_unauthorized
import retailhub.core.ui.generated.resources.error_unknown
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class NetworkResultExtensionsTest {
    @Test
    fun `ApiError maps error message to description`() {
        val expectedMessage = "error message"

        val failure = NetworkResult.Failure.ApiError(
            error = ApiErrorResponse(expectedMessage)
        )

        val result = failure.toUiError()

        assertEquals(expectedMessage, result.description)
        assertEquals(Res.string.error_unknown, result.descriptionId)
    }

    @Test
    fun `Forbidden maps to forbidden error`() {
        val result = NetworkResult.Failure.Forbidden.toUiError()

        assertEquals(Res.string.error_forbidden, result.descriptionId)
    }

    @Test
    fun `NoInternet maps to no internet error`() {
        val result = NetworkResult.Failure.NoInternet.toUiError()

        assertEquals(Res.string.error_no_internet, result.descriptionId)
    }

    @Test
    fun `Serialization maps to serialization error`() {
        val failure = NetworkResult.Failure.Serialization(
            message = "Invalid JSON"
        )

        val result = failure.toUiError()

        assertEquals(Res.string.error_serialization, result.descriptionId)
    }

    @Test
    fun `Server maps to server error`() {
        val failure = NetworkResult.Failure.Server(
            code = 500,
            message = "Internal server error"
        )

        val result = failure.toUiError()

        assertEquals(Res.string.error_server, result.descriptionId)
    }

    @Test
    fun `Timeout maps to timeout error`() {
        val result = NetworkResult.Failure.Timeout.toUiError()

        assertEquals(Res.string.error_timeout, result.descriptionId)
    }

    @Test
    fun `Unauthorized maps to unauthorized error`() {
        val result = NetworkResult.Failure.Unauthorized.toUiError()

        assertEquals(Res.string.error_unauthorized, result.descriptionId)
    }

    @Test
    fun `Unknown maps to default error`() {
        val failure = NetworkResult.Failure.Unknown(
            message = "Something unexpected happened"
        )

        val result = failure.toUiError()

        assertEquals(UiError(), result)
    }
}
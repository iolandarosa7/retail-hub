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
    fun apiError_mapsErrorMessage_toDescription() {
        val expectedMessage = "error message"

        val failure = NetworkResult.Failure.ApiError(
            error = ApiErrorResponse(expectedMessage)
        )

        val result = failure.toUiError()

        assertEquals(expectedMessage, result.description)
        assertEquals(Res.string.error_unknown, result.descriptionId)
    }

    @Test
    fun forbidden_maps_toForbiddenError() {
        val result = NetworkResult.Failure.Forbidden.toUiError()

        assertEquals(Res.string.error_forbidden, result.descriptionId)
    }

    @Test
    fun noInternet_maps_toNoInternetError() {
        val result = NetworkResult.Failure.NoInternet.toUiError()

        assertEquals(Res.string.error_no_internet, result.descriptionId)
    }

    @Test
    fun serialization_maps_toSerializationError() {
        val failure = NetworkResult.Failure.Serialization(
            message = "Invalid JSON"
        )

        val result = failure.toUiError()

        assertEquals(Res.string.error_serialization, result.descriptionId)
    }

    @Test
    fun server_maps_toServerError() {
        val failure = NetworkResult.Failure.Server(
            code = 500,
            message = "Internal server error"
        )

        val result = failure.toUiError()

        assertEquals(Res.string.error_server, result.descriptionId)
    }

    @Test
    fun timeout_maps_toTimeoutError() {
        val result = NetworkResult.Failure.Timeout.toUiError()

        assertEquals(Res.string.error_timeout, result.descriptionId)
    }

    @Test
    fun unauthorized_maps_toUnauthorizedError() {
        val result = NetworkResult.Failure.Unauthorized.toUiError()

        assertEquals(Res.string.error_unauthorized, result.descriptionId)
    }

    @Test
    fun unknown_maps_toDefaultError() {
        val failure = NetworkResult.Failure.Unknown(
            message = "Something unexpected happened"
        )

        val result = failure.toUiError()

        assertEquals(UiError(), result)
    }
}
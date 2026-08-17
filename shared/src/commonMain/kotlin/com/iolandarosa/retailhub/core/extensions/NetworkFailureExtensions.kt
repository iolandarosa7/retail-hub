package com.iolandarosa.retailhub.core.extensions

import com.iolandarosa.retailhub.core.models.NetworkResult
import com.iolandarosa.retailhub.core.models.UiError
import retailhub.shared.generated.resources.Res
import retailhub.shared.generated.resources.error_forbidden
import retailhub.shared.generated.resources.error_no_internet
import retailhub.shared.generated.resources.error_serialization
import retailhub.shared.generated.resources.error_server
import retailhub.shared.generated.resources.error_timeout
import retailhub.shared.generated.resources.error_unauthorized

fun NetworkResult.Failure.toUiError(): UiError =
    when (this) {
        is NetworkResult.Failure.ApiError -> UiError(description = error.message)
        NetworkResult.Failure.Forbidden -> UiError(descriptionId = Res.string.error_forbidden)
        NetworkResult.Failure.NoInternet -> UiError(descriptionId = Res.string.error_no_internet)
        is NetworkResult.Failure.Serialization -> UiError(descriptionId = Res.string.error_serialization)
        is NetworkResult.Failure.Server -> UiError(descriptionId = Res.string.error_server)
        NetworkResult.Failure.Timeout -> UiError(descriptionId = Res.string.error_timeout)
        NetworkResult.Failure.Unauthorized -> UiError(descriptionId = Res.string.error_unauthorized)
        is NetworkResult.Failure.Unknown -> UiError()
    }

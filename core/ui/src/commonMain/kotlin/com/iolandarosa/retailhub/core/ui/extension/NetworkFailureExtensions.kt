package com.iolandarosa.retailhub.core.ui.extension

import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.core.ui.error.UiError
import retailhub.core.ui.generated.resources.Res
import retailhub.core.ui.generated.resources.error_forbidden
import retailhub.core.ui.generated.resources.error_no_internet
import retailhub.core.ui.generated.resources.error_serialization
import retailhub.core.ui.generated.resources.error_server
import retailhub.core.ui.generated.resources.error_timeout
import retailhub.core.ui.generated.resources.error_unauthorized

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

/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.extension

import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.core.ui.error.UiError
import retailhub.core.ui.generated.resources.Res
import retailhub.core.ui.generated.resources.error_forbidden
import retailhub.core.ui.generated.resources.error_forbidden_title
import retailhub.core.ui.generated.resources.error_no_internet
import retailhub.core.ui.generated.resources.error_no_internet_title
import retailhub.core.ui.generated.resources.error_not_found
import retailhub.core.ui.generated.resources.error_not_found_title
import retailhub.core.ui.generated.resources.error_serialization
import retailhub.core.ui.generated.resources.error_server
import retailhub.core.ui.generated.resources.error_server_title
import retailhub.core.ui.generated.resources.error_timeout
import retailhub.core.ui.generated.resources.error_timeout_title
import retailhub.core.ui.generated.resources.error_unauthorized
import retailhub.core.ui.generated.resources.error_unauthorized_title
import retailhub.core.ui.generated.resources.error_unknown_title

fun NetworkResult.Failure.toUiError(): UiError =
    when (this) {
        is NetworkResult.Failure.ApiError -> {
            UiError(
                titleId = Res.string.error_unknown_title,
                description = error.message,
            )
        }

        NetworkResult.Failure.Forbidden -> {
            UiError(
                titleId = Res.string.error_forbidden_title,
                descriptionId = Res.string.error_forbidden,
                hasRetry = false,
            )
        }

        NetworkResult.Failure.NoInternet -> {
            UiError(
                titleId = Res.string.error_no_internet_title,
                descriptionId = Res.string.error_no_internet,
            )
        }

        is NetworkResult.Failure.Serialization -> {
            UiError(
                titleId = Res.string.error_unknown_title,
                descriptionId = Res.string.error_serialization,
            )
        }

        is NetworkResult.Failure.Server -> {
            UiError(
                titleId = Res.string.error_server_title,
                descriptionId = Res.string.error_server,
            )
        }

        NetworkResult.Failure.Timeout -> {
            UiError(
                titleId = Res.string.error_timeout_title,
                descriptionId = Res.string.error_timeout,
            )
        }

        NetworkResult.Failure.Unauthorized -> {
            UiError(
                titleId = Res.string.error_unauthorized_title,
                descriptionId = Res.string.error_unauthorized,
                hasRetry = false,
            )
        }

        NetworkResult.Failure.NotFound -> {
            UiError(
                titleId = Res.string.error_not_found_title,
                descriptionId = Res.string.error_not_found,
                hasRetry = false,
            )
        }

        is NetworkResult.Failure.Unknown -> {
            UiError()
        }
    }

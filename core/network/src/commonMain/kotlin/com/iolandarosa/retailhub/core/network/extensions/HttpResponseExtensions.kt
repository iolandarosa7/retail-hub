/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.network.extensions

import com.iolandarosa.retailhub.core.model.ApiErrorResponse
import com.iolandarosa.retailhub.core.model.NetworkResult
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.serialization.SerializationException
import kotlin.runCatching

suspend inline fun <reified T> HttpResponse.handleResponse(): NetworkResult<T> =
    when {
        status.isSuccess() -> {
            try {
                val result = if (T::class == Unit::class) Unit as T else body<T>()
                NetworkResult.Success(result)
            } catch (e: SerializationException) {
                NetworkResult.Failure.Serialization("${status.value}: ${e.message}")
            } catch (e: Exception) {
                NetworkResult.Failure.Unknown("${status.value}: ${e.message}")
            }
        }

        status == HttpStatusCode.Unauthorized -> {
            NetworkResult.Failure.Unauthorized
        }

        status == HttpStatusCode.Forbidden -> {
            NetworkResult.Failure.Forbidden
        }

        status.value in 500..599 -> {
            NetworkResult.Failure.Server(code = status.value, message = bodyAsText())
        }

        else -> {
            runCatching { body<ApiErrorResponse>() }.fold(
                onSuccess = { NetworkResult.Failure.ApiError(it) },
                onFailure = { NetworkResult.Failure.Serialization("HTTP ${status.value}: ${it.message}") },
            )
        }
    }

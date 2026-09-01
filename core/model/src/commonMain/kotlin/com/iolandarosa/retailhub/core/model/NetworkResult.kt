/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.model

sealed interface NetworkResult<out T> {
    data class Success<T>(
        val data: T,
    ) : NetworkResult<T>

    sealed interface Failure : NetworkResult<Nothing> {
        data class Server(
            val code: Int,
            val message: String? = null,
        ) : Failure

        data object Unauthorized : Failure

        data object Forbidden : Failure

        data object NoInternet : Failure

        data object Timeout : Failure

        data class Serialization(
            val message: String? = null,
        ) : Failure

        data class ApiError(
            val error: ApiErrorResponse,
        ) : Failure

        data class Unknown(
            val message: String? = null,
        ) : Failure
    }

    fun <R> map(transform: (T) -> R): NetworkResult<R> =
        when (this) {
            is Success -> Success(transform(data))
            is Failure -> this
        }
}

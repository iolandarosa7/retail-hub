/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class NetworkResultTest {
    @Test
    fun success_map_hasTransformedValue() {
        val result: NetworkResult<Int> = NetworkResult.Success(10)

        val mapped = result.map { it.toString() }

        assertEquals(
            NetworkResult.Success("10"),
            mapped,
        )
    }

    @Test
    fun failure_map_doesNotChanges() {
        val failures =
            listOf(
                NetworkResult.Failure.Server(500, "Server error"),
                NetworkResult.Failure.Unauthorized,
                NetworkResult.Failure.Forbidden,
                NetworkResult.Failure.NoInternet,
                NetworkResult.Failure.Timeout,
                NetworkResult.Failure.Serialization("Invalid JSON"),
                NetworkResult.Failure.ApiError(
                    ApiErrorResponse("Something went wrong"),
                ),
                NetworkResult.Failure.Unknown("Unknown error"),
            )

        failures.forEach { failure ->
            val mapped = failure.map { "transformed" }

            assertSame(failure, mapped)
        }
    }
}

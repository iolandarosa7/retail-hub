package com.iolandarosa.retailhub.core.extensions

import com.iolandarosa.retailhub.core.models.NetworkResult
import io.ktor.client.HttpClient
import io.ktor.client.statement.HttpResponse

suspend inline fun <reified T> HttpClient.safeRequest(
    crossinline block: suspend HttpClient.() -> HttpResponse
): NetworkResult<T> {
    return try {
        val response = block()
        response.handleResponse<T>()
    } catch (e: Exception) {
        NetworkResult.Failure("Network error: ${e.message}")
    }
}
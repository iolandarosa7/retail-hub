package com.iolandarosa.retailhub.core.common.extensions

import com.iolandarosa.retailhub.core.common.model.NetworkResult
import io.ktor.client.HttpClient
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException

suspend inline fun <reified T> HttpClient.safeRequest(
    crossinline block: suspend HttpClient.() -> HttpResponse
): NetworkResult<T> {
    return try {
        val response = block()
        response.handleResponse<T>()
    } catch (_: UnresolvedAddressException) {
        NetworkResult.Failure.NoInternet
    } catch (_: ConnectTimeoutException) {
        NetworkResult.Failure.Timeout
    } catch (_: HttpRequestTimeoutException) {
        NetworkResult.Failure.Timeout
    } catch (_: SocketTimeoutException) {
        NetworkResult.Failure.Timeout
    } catch (e: SerializationException) {
        NetworkResult.Failure.Serialization(message = e.message)
    } catch (e: Exception) {
        NetworkResult.Failure.Unknown(message = e.message)
    }
}
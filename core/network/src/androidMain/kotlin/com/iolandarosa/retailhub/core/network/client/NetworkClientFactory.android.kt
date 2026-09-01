/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.network.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp

actual fun platformHttpClient(): HttpClient = HttpClient(OkHttp)

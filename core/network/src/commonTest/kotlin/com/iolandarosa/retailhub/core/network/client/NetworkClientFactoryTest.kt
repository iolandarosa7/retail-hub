package com.iolandarosa.retailhub.core.network.client

import com.iolandarosa.retailhub.core.network.endpoint.Endpoints
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.get
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class NetworkClientFactoryTest {

    @Test
    fun createHttpClient_appliesAllRequiredPlugins() = runTest {
        val engine = MockEngine { _ ->
            respondOk()
        }

        val client = createHttpClient(engine)

        assertNotNull(client.plugin(ContentNegotiation), "ContentNegotiation should be installed")
        assertNotNull(client.plugin(Logging), "Logging should be installed")
        assertNotNull(client.plugin(DefaultRequest), "DefaultRequest should be installed")
        assertNotNull(client.plugin(HttpTimeout), "HttpTimeout should be installed")
    }

    @Test
    fun createHttpClient_configuresBaseUrlCorrectly() = runTest {
        var capturedUrl = ""
        val engine = MockEngine { request ->
            capturedUrl = request.url.toString()
            respondOk()
        }

        val client = createHttpClient(engine)
        
        // This should use the BASE_URL from configuration
        client.get("test")
        
        assertTrue(capturedUrl.startsWith(Endpoints.BASE_URL), "URL should start with Base URL: $capturedUrl")
        assertTrue(capturedUrl.endsWith("/test"), "URL should end with /test: $capturedUrl")
    }
}

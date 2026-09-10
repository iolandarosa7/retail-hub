/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.domain.interactors

import com.iolandarosa.retailhub.core.datastore.domain.TokenManager
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class LogoutUseCaseTest {
    private val tokenManager = mock<TokenManager>()
    private val useCase = LogoutUseCaseImpl(tokenManager)

    @Test
    fun success_invoke_hasExpectedResponse() =
        runTest {
            everySuspend { tokenManager.clearTokens() } returns Unit

            val result = useCase()

            assertEquals(Unit, result)

            verifySuspend { tokenManager.clearTokens() }
        }
}

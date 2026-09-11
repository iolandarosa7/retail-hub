/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.domain.interactors

import com.iolandarosa.retailhub.core.datastore.domain.TokenManager
import com.iolandarosa.retailhub.features.auth.domain.repository.AuthenticationRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class LogoutUseCaseTest {
    private val repository = mock<AuthenticationRepository>()
    private val useCase = LogoutUseCaseImpl(repository)

    @Test
    fun success_invoke_hasExpectedResponse() =
        runTest {
            everySuspend { repository.logout() } returns Unit

            val result = useCase()

            assertEquals(Unit, result)

            verifySuspend { repository.logout() }
        }
}

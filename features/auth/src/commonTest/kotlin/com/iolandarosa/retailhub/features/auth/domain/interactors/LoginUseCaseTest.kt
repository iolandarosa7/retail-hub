/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.domain.interactors

import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.features.auth.domain.repository.AuthenticationRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class LoginUseCaseTest {
    private val repository = mock<AuthenticationRepository>()
    private val useCase = LoginUseCaseImpl(repository)

    @Test
    fun success_invoke_hasExpectedResponse() =
        runTest {
            val username = "username"
            val password = "password"

            everySuspend { repository.login(any(), any()) } returns NetworkResult.Success(Unit)

            val result = useCase(username, password)

            assertEquals(NetworkResult.Success(Unit), result)

            verifySuspend { repository.login(username, password) }
        }
}

/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.user.domain.interactors

import com.iolandarosa.retailhub.core.user.domain.repository.UserRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetLocalUserImageUseCaseTest {
    private val repository = mock<UserRepository>()
    private val useCase = GetLocalUserImageUseCaseImpl(repository)

    @Test
    fun useCase_invoke_hasExpectedResult() =
        runTest {
            val userId = 123
            val expectedBytes = byteArrayOf(1, 2, 3)
            everySuspend { repository.getLocalUserImage(userId) } returns expectedBytes

            val result = useCase(userId)

            assertEquals(expectedBytes, result)
            verifySuspend { repository.getLocalUserImage(userId) }
        }
}

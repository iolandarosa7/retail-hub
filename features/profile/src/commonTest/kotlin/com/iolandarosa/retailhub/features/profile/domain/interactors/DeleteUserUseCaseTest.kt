/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.domain.interactors

import com.iolandarosa.retailhub.features.profile.domain.repository.ProfileRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DeleteUserUseCaseTest {
    private val repository = mock<ProfileRepository>()
    private val useCase = DeleteUserUseCaseImpl(repository)

    @Test
    fun invoke_repositoryReturnsTrue_returnsTrue() =
        runTest {
            val userId = 1
            everySuspend { repository.deleteUser(userId) } returns true

            val result = useCase(userId)

            assertEquals(true, result)
            verifySuspend { repository.deleteUser(userId) }
        }

    @Test
    fun invoke_repositoryReturnsFalse_returnsFalse() =
        runTest {
            val userId = 1
            everySuspend { repository.deleteUser(userId) } returns false

            val result = useCase(userId)

            assertEquals(false, result)
            verifySuspend { repository.deleteUser(userId) }
        }
}

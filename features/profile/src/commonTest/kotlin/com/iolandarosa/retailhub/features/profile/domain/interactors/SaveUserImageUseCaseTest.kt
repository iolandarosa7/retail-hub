/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.domain.interactors

import com.iolandarosa.retailhub.core.storage.domain.model.ImageStorageResult
import com.iolandarosa.retailhub.features.profile.domain.repository.ProfileRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SaveUserImageUseCaseTest {
    private val repository = mock<ProfileRepository>()
    private val useCase = SaveUserImageUseCaseImpl(repository)

    @Test
    fun useCase_invoke_hasExpectedResult() =
        runTest {
            val userId = 123
            val bytes = byteArrayOf(4, 5, 6)
            val expectedResult = ImageStorageResult.Success
            everySuspend { repository.saveUserImage(userId, bytes) } returns expectedResult

            val result = useCase(userId, bytes)

            assertEquals(expectedResult, result)
            verifySuspend { repository.saveUserImage(userId, bytes) }
        }
}

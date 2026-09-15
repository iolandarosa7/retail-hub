/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.domain.interactors

import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.features.profile.domain.repository.ProfileRepository
import com.iolandarosa.retailhub.features.profile.utils.TestUser
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetAuthUserUseCaseTest {
    private val repository = mock<ProfileRepository>()
    private val useCase = GetAuthUserUseCaseImpl(repository)

    @Test
    fun success_invoke_hasExpectedResponse() =
        runTest {
            val data = TestUser.user
            everySuspend { repository.getAuthUser() } returns NetworkResult.Success(data)

            val result = useCase()

            assertEquals(NetworkResult.Success(data), result)

            verifySuspend { repository.getAuthUser() }
        }
}

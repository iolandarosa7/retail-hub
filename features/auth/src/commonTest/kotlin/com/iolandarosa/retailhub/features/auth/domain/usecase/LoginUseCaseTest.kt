package com.iolandarosa.retailhub.features.auth.domain.usecase

import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.features.auth.domain.model.User
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
    private val useCase = LoginUseCase(repository)

    @Test
    fun `repository login returns user when service succeeds`() = runTest {
        val username = "username"
        val password = "password"

        val user = User(
            id = 1,
            name = "name",
        )

        everySuspend { repository.login(any(), any())} returns NetworkResult.Success(user)

        val result = useCase(username, password)

        assertEquals(NetworkResult.Success(user), result)

        verifySuspend { repository.login(username, password) }
    }
}
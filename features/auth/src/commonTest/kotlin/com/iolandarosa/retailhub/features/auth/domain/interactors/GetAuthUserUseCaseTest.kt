package com.iolandarosa.retailhub.features.auth.domain.interactors

import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.features.auth.domain.model.User
import com.iolandarosa.retailhub.features.auth.domain.repository.AuthenticationRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetAuthUserUseCaseTest {
    private val repository = mock<AuthenticationRepository>()
    private val useCase = GetAuthUserUseCaseImpl(repository)

    @Test
    fun success_invoke_hasExpectedResponse() =
        runTest {
            val data =
                User(
                    name = "name",
                    image = "image",
                    role = "role",
                    email = "email",
                    phone = "phone",
                    age = 30,
                    gender = "male",
                    birthDate = "2000-01-01",
                    bloodGroup = "A+",
                    height = 180.0,
                    weight = 80.0,
                    eyeColor = "brown",
                    hairColor = "black",
                    hairType = "straight",
                    address = "address",
                )
            everySuspend { repository.getAuthUser() } returns NetworkResult.Success(data)

            val result = useCase()

            assertEquals(NetworkResult.Success(data), result)

            verifySuspend { repository.getAuthUser() }
        }
}

/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.user.domain.interactors

import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.core.user.domain.model.Address
import com.iolandarosa.retailhub.core.user.domain.model.Coordinates
import com.iolandarosa.retailhub.core.user.domain.model.User
import com.iolandarosa.retailhub.core.user.domain.repository.UserRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetAuthUserUseCaseTest {
    private val repository = mock<UserRepository>()
    private val useCase = GetAuthUserUseCaseImpl(repository)

    @Test
    fun success_invoke_hasExpectedResponse() =
        runTest {
            val data =
                User(
                    id = 1,
                    name = "John Doe",
                    image = "image_url",
                    role = "admin",
                    email = "john@example.com",
                    phone = "123456",
                    age = 30,
                    gender = "male",
                    birthDate = "2000-01-01",
                    bloodGroup = "A+",
                    height = 180.0,
                    weight = 80.0,
                    eyeColor = "brown",
                    hairColor = "black",
                    hairType = "straight",
                    address =
                        Address(
                            street = "address",
                            city = "city",
                            state = "state",
                            stateCode = "stateCode",
                            postalCode = "postalCode",
                            coordinates = Coordinates(lat = 1.0, lng = 1.0),
                            country = "country",
                        ),
                )

            everySuspend { repository.getAuthUser() } returns NetworkResult.Success(data)

            val result = useCase()

            assertEquals(NetworkResult.Success(data), result)

            verifySuspend { repository.getAuthUser() }
        }
}

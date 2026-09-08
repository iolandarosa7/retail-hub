/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.presentation.profile

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runComposeUiTest
import com.iolandarosa.retailhub.features.auth.domain.model.User
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ContactCardTest {
    @Test
    fun contactCardDisplaysEmailAndPhone() =
        runComposeUiTest {
            val user =
                User(
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
                    address = "123 Main St",
                )

            setContent {
                ContactCard(user = user)
            }

            onNodeWithText("john@example.com").assertIsDisplayed()
            onNodeWithText("123456").assertIsDisplayed()
        }
}

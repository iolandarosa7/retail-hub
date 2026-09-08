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
class PhysicalInfoCardTest {
    @Test
    fun physicalInfoCardDisplaysDetails() =
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
                PhysicalInfoCard(user = user)
            }

            onNodeWithText("180.0 cm").assertIsDisplayed()
            onNodeWithText("80.0 kg").assertIsDisplayed()
            onNodeWithText("A+").assertIsDisplayed()
            onNodeWithText("brown").assertIsDisplayed()
            onNodeWithText("black").assertIsDisplayed()
            onNodeWithText("straight").assertIsDisplayed()
        }
}

/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.presentation.profile.components

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runComposeUiTest
import com.iolandarosa.retailhub.features.profile.utils.TestUser
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class PhysicalInfoCardTest {
    @Test
    fun physicalInfoCardDisplaysDetails() =
        runComposeUiTest {
            val user = TestUser.user

            setContent {
                PhysicalInfoCard(user = user)
            }

            onNodeWithText("180.0 cm").assertIsDisplayed()
            onNodeWithText("80.0 kg").assertIsDisplayed()
            onNodeWithText(user.bloodGroup).assertIsDisplayed()
            onNodeWithText(user.eyeColor).assertIsDisplayed()
            onNodeWithText(user.hairColor).assertIsDisplayed()
            onNodeWithText(user.hairType).assertIsDisplayed()
        }
}

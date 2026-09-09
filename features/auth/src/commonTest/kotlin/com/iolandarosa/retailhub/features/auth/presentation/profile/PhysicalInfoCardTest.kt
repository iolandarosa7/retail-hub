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
import com.iolandarosa.retailhub.features.auth.utils.TestUser
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

            onNodeWithText("${user.height} cm").assertIsDisplayed()
            onNodeWithText("${user.height} kg").assertIsDisplayed()
            onNodeWithText(user.bloodGroup).assertIsDisplayed()
            onNodeWithText(user.eyeColor).assertIsDisplayed()
            onNodeWithText(user.hairColor).assertIsDisplayed()
            onNodeWithText(user.hairType).assertIsDisplayed()
        }
}

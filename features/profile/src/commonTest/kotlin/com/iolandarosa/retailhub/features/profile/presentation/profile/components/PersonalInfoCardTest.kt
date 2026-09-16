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
class PersonalInfoCardTest {
    @Test
    fun personalInfoCardDisplaysDetails() =
        runComposeUiTest {
            val user = TestUser.user

            setContent {
                PersonalInfoCard(user = user)
            }

            onNodeWithText(user.birthDate).assertIsDisplayed()
            onNodeWithText("${user.age}").assertIsDisplayed()
            onNodeWithText(user.gender).assertIsDisplayed()
        }
}

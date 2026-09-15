/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.presentation.profile

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runComposeUiTest
import com.iolandarosa.retailhub.features.profile.utils.TestUser
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ContactCardTest {
    @Test
    fun contactCardDisplaysEmailAndPhone() =
        runComposeUiTest {
            val user = TestUser.user

            setContent {
                ContactCard(user = user)
            }

            onNodeWithText(user.email).assertIsDisplayed()
            onNodeWithText(user.phone).assertIsDisplayed()
        }
}

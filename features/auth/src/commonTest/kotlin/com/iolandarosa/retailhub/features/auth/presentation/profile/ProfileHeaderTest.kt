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
class ProfileHeaderTest {
    @Test
    fun profileHeaderDisplaysUserNameAndRole() =
        runComposeUiTest {
            val user = TestUser.user

            setContent {
                ProfileHeader(user = user)
            }

            onNodeWithText(user.name).assertIsDisplayed()
            onNodeWithText(user.role).assertIsDisplayed()
        }
}

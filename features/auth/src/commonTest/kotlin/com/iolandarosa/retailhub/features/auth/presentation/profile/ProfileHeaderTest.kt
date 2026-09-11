/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.presentation.profile

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import com.iolandarosa.retailhub.features.auth.utils.TestUser
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ProfileHeaderTest {
    @Test
    fun user_componentLoaded_hasExpectedValues() =
        runComposeUiTest {
            val user = TestUser.user

            setContent {
                ProfileHeader(user = user)
            }

            onNodeWithText(user.name).assertIsDisplayed()
            onNodeWithText(user.role).assertIsDisplayed()

            onNodeWithContentDescription("Update profile picture")
                .assertIsNotDisplayed()
        }

    @Test
    fun isEnabled_onPhotoClick_expectedCallbackCalled() =
        runComposeUiTest {
            val user = TestUser.user
            var callbackCalled = false

            setContent {
                ProfileHeader(user = user, isEnabled = true, onPhotoClick = { callbackCalled = true })
            }

            onNodeWithContentDescription("Update profile picture")
                .assertIsDisplayed()
                .assertIsEnabled()
                .performClick()

            waitUntil { callbackCalled }
        }

    @Test
    fun isDisabled_onPhotoClick_expectedCallbackCalled() =
        runComposeUiTest {
            val user = TestUser.user

            setContent {
                ProfileHeader(user = user, isEnabled = false, onPhotoClick = { })
            }

            onNodeWithContentDescription("Update profile picture")
                .assertIsDisplayed()
                .assertIsNotEnabled()
        }

    @Test
    fun defaultState_componentLoaded_hasExpectedUI() =
        runComposeUiTest {
            setContent {
                ProfileHeader()
            }

            onNodeWithContentDescription("Update profile picture")
                .assertIsNotDisplayed()

            onNodeWithContentDescription("Loading User Profile")
                .assertIsDisplayed()
        }
}

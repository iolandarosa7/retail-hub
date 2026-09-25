/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.presentation.profile.components

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.v2.runComposeUiTest
import com.iolandarosa.retailhub.core.ui.error.UiError
import com.iolandarosa.retailhub.core.ui.form.FormState
import com.iolandarosa.retailhub.features.profile.presentation.profile.LoginForm
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class UnauthenticatedContentTest {
    @Test
    fun initialState_componentLoaded_expectedUiDisplayed() =
        runComposeUiTest {
            val formState =
                FormState(
                    fields =
                        LoginForm.get(
                            onValueChanged = {},
                            onActionDone = {},
                        ),
                )

            setContent {
                UnauthenticatedContent(
                    formState,
                    isEnabled = true,
                    error = null,
                    onSignInClick = {},
                )
            }

            onNodeWithText("Username")
                .assertIsDisplayed()
                .assertIsEnabled()

            onNodeWithText("Password")
                .assertIsDisplayed()
                .assertIsEnabled()

            onNodeWithText("Sign in")
                .assertIsDisplayed()
                .assertIsEnabled()
        }

    @Test
    fun initialState_clickLogin_expectedCallbackCalled() =
        runComposeUiTest {
            var onValueChanged = false
            var callbackCalled = false

            val formState =
                FormState(
                    fields =
                        LoginForm.get(
                            onValueChanged = { onValueChanged = true },
                            onActionDone = {},
                        ),
                )

            setContent {
                UnauthenticatedContent(
                    formState,
                    isEnabled = true,
                    error = null,
                    onSignInClick = { callbackCalled = true },
                )
            }

            onNodeWithText("Username")
                .assertIsDisplayed()
                .assertIsEnabled()

            onNodeWithText("Password")
                .assertIsDisplayed()
                .assertIsEnabled()

            onNodeWithText("Sign in")
                .assertIsDisplayed()
                .assertIsEnabled()

            onNodeWithText("Username").performTextInput("username")

            waitUntil { onValueChanged }

            onNodeWithText("Sign in").performClick()

            waitUntil { callbackCalled }
        }

    @Test
    fun error_componentLoaded_displayError() =
        runComposeUiTest {
            val errorMessage = "error message"
            val formState =
                FormState(
                    fields =
                        LoginForm.get(
                            onValueChanged = {},
                            onActionDone = {},
                        ),
                )

            setContent {
                UnauthenticatedContent(
                    formState,
                    isEnabled = true,
                    error = UiError(description = errorMessage),
                    onSignInClick = {},
                )
            }

            onNodeWithText(errorMessage)
                .assertIsDisplayed()
        }

    @Test
    fun isEnabledFalse_componentLoaded_expectedDisabledButtonDisplayed() =
        runComposeUiTest {
            val formState =
                FormState(
                    fields =
                        LoginForm.get(
                            onValueChanged = {},
                            onActionDone = {},
                        ),
                )

            setContent {
                UnauthenticatedContent(
                    formState,
                    isEnabled = false,
                    error = null,
                    onSignInClick = {},
                )
            }

            onNodeWithText("Sign in")
                .assertIsDisplayed()
                .assertIsNotEnabled()
        }
}

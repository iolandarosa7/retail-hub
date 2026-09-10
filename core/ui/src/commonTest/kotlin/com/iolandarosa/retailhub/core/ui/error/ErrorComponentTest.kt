/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.error

import androidx.compose.material3.Text
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runComposeUiTest
import kotlin.test.Test

class ErrorComponentTest {
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun initialState_componentLoaded_displaysExpectedComponent() =
        runComposeUiTest {
            val title = "Error"
            val description = "description"
            val trailingText = "trailingText"

            setContent {
                ErrorComponent(title = title, description = description, trailingContent = { Text(trailingText) })
            }

            onNodeWithText(title).assertIsDisplayed()
            onNodeWithText(description).assertIsDisplayed()
            onNodeWithText(trailingText).assertIsDisplayed()
        }
}

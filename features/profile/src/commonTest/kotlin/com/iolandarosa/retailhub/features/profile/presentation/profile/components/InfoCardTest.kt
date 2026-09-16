/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.presentation.profile.components

import androidx.compose.material3.Text
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runComposeUiTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class InfoCardTest {
    @Test
    fun infoCardDisplaysTitleAndContent() =
        runComposeUiTest {
            setContent {
                InfoCard(title = "Title") {
                    Text(text = "Content")
                }
            }

            onNodeWithText("TITLE").assertIsDisplayed()
            onNodeWithText("Content").assertIsDisplayed()
        }
}

/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.progress

import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.unit.dp
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class SkeletonTest {
    @Test
    fun defaultContentDescription_componentLoaded_expectComponentDisplayed() =
        runComposeUiTest {
            setContent { Skeleton(modifier = Modifier.size(100.dp)) }

            onNodeWithContentDescription("Loading").assertIsDisplayed()
        }

    @Test
    fun customContentDescription_componentLoaded_expectComponentDisplayed() =
        runComposeUiTest {
            val contentDescription = "customContentDescription"

            setContent { Skeleton(contentDescription = contentDescription, modifier = Modifier.size(100.dp)) }

            onNodeWithContentDescription(contentDescription).assertIsDisplayed()
        }
}

/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.images

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import com.iolandarosa.retailhub.core.ui.permissions.AppPermission
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ImagePickerBottomSheetTest {
    @Composable
    fun TestImagePickerBottomSheet(onClick: (AppPermission) -> Unit = {}) =
        ImagePickerBottomSheet(
            onDismiss = {},
            onClick = onClick,
        )

    @Test
    fun initialState_componentLoaded_expectedComponentDisplayed() =
        runComposeUiTest {
            setContent { TestImagePickerBottomSheet() }

            onNodeWithText("Take a photo or pick an image from the gallery")
                .assertIsDisplayed()

            onNodeWithText("Gallery")
                .assertIsDisplayed()
                .assertIsEnabled()

            onNodeWithText("Camera")
                .assertIsDisplayed()
                .assertIsEnabled()
        }

    @Test
    fun componentLoaded_galleryClick_expectCallbackCalled() =
        runComposeUiTest {
            var permission: AppPermission? = null

            setContent { TestImagePickerBottomSheet(onClick = { permission = it }) }

            onNodeWithText("Gallery")
                .performClick()

            waitUntil { permission == AppPermission.Gallery }
        }

    @Test
    fun componentLoaded_cameraClick_expectCallbackCalled() =
        runComposeUiTest {
            var permission: AppPermission? = null

            setContent { TestImagePickerBottomSheet(onClick = { permission = it }) }

            onNodeWithText("Camera")
                .performClick()

            waitUntil { permission == AppPermission.Camera }
        }
}

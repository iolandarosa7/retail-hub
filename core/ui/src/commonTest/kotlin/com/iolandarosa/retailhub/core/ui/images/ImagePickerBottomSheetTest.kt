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
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ImagePickerBottomSheetTest {
    @Composable
    fun TestImagePickerBottomSheet(onClick: (PermissionType) -> Unit = {}) =
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
            var permissionType: PermissionType? = null

            setContent { TestImagePickerBottomSheet(onClick = { permissionType = it }) }

            onNodeWithText("Gallery")
                .performClick()

            waitUntil { permissionType == PermissionType.GALLERY }
        }

    @Test
    fun componentLoaded_cameraClick_expectCallbackCalled() =
        runComposeUiTest {
            var permissionType: PermissionType? = null

            setContent { TestImagePickerBottomSheet(onClick = { permissionType = it }) }

            onNodeWithText("Camera")
                .performClick()

            waitUntil { permissionType == PermissionType.CAMERA }
        }
}

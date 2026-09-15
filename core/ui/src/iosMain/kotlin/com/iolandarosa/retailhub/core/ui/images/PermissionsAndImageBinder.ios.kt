/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.images

import androidx.compose.runtime.Composable
import com.iolandarosa.retailhub.core.ui.permissions.PermissionController

@Composable
actual fun InitializePermissionsAndPicker(
    permissionController: PermissionController,
    imagePickerController: ImagePickerController,
) {
    // No-op: iOS uses native APIs directly from the delegate without layout binders
}

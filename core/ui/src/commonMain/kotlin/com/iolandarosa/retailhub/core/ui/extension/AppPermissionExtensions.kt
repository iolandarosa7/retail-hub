/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.extension

import com.iolandarosa.retailhub.core.model.PreferencesKey
import com.iolandarosa.retailhub.core.ui.images.ImageSource
import com.iolandarosa.retailhub.core.ui.permissions.AppPermission
import com.iolandarosa.retailhub.core.ui.permissions.PermissionDialog
import com.iolandarosa.retailhub.core.ui.permissions.PermissionDialogActionType
import retailhub.core.ui.generated.resources.Res
import retailhub.core.ui.generated.resources.camera_permission_denied_description
import retailhub.core.ui.generated.resources.camera_permission_denied_title
import retailhub.core.ui.generated.resources.gallery_permission_denied_description
import retailhub.core.ui.generated.resources.gallery_permission_denied_title
import retailhub.core.ui.generated.resources.open_settings

fun AppPermission.toImageSource(): ImageSource =
    when (this) {
        AppPermission.Camera -> ImageSource.Camera
        AppPermission.Gallery -> ImageSource.Gallery
    }

fun AppPermission.toOpenSettingsDialog() =
    when (this) {
        AppPermission.Camera -> {
            PermissionDialog(
                titleId = Res.string.camera_permission_denied_title,
                descriptionId = Res.string.camera_permission_denied_description,
                confirmButtonLabelId = Res.string.open_settings,
                type = PermissionDialogActionType.OpenSettings,
            )
        }

        AppPermission.Gallery -> {
            PermissionDialog(
                titleId = Res.string.gallery_permission_denied_title,
                descriptionId = Res.string.gallery_permission_denied_description,
                confirmButtonLabelId = Res.string.open_settings,
                type = PermissionDialogActionType.OpenSettings,
            )
        }
    }

fun AppPermission.toPreferencesKey(): String =
    when (this) {
        AppPermission.Camera -> PreferencesKey.CAMERA
        AppPermission.Gallery -> PreferencesKey.GALLERY
    }

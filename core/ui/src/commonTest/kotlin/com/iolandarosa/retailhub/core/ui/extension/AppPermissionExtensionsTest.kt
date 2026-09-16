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
import kotlin.test.Test
import kotlin.test.assertEquals

class AppPermissionExtensionsTest {
    @Test
    fun appPermissionCamera_toImageSource_hasExpectedValue() {
        assertEquals(ImageSource.Camera, AppPermission.Camera.toImageSource())
    }

    @Test
    fun appPermissionGallery_toImageSource_hasExpectedValue() {
        assertEquals(ImageSource.Gallery, AppPermission.Gallery.toImageSource())
    }

    @Test
    fun appPermissionCamera_toOpenSettingsDialog_hasExpectedValue() {
        assertEquals(
            PermissionDialog(
                titleId = Res.string.camera_permission_denied_title,
                descriptionId = Res.string.camera_permission_denied_description,
                confirmButtonLabelId = Res.string.open_settings,
                type = PermissionDialogActionType.OpenSettings,
            ),
            AppPermission.Camera.toOpenSettingsDialog(),
        )
    }

    @Test
    fun appPermissionGallery_toOpenSettingsDialog_hasExpectedValue() {
        assertEquals(
            PermissionDialog(
                titleId = Res.string.gallery_permission_denied_title,
                descriptionId = Res.string.gallery_permission_denied_description,
                confirmButtonLabelId = Res.string.open_settings,
                type = PermissionDialogActionType.OpenSettings,
            ),
            AppPermission.Gallery.toOpenSettingsDialog(),
        )
    }

    @Test
    fun appPermissionCamera_toPreferencesKey_hasExpectedValue() {
        assertEquals(PreferencesKey.CAMERA, AppPermission.Camera.toPreferencesKey())
    }

    @Test
    fun appPermissionGallery_toPreferencesKey_hasExpectedValue() {
        assertEquals(PreferencesKey.GALLERY, AppPermission.Gallery.toPreferencesKey())
    }
}

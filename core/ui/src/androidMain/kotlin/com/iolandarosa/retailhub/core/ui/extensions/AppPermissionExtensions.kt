/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.extensions

import android.Manifest
import com.iolandarosa.retailhub.core.ui.permissions.AppPermission

fun AppPermission.toAndroid(): String =
    when (this) {
        AppPermission.Camera -> Manifest.permission.CAMERA
        AppPermission.Gallery -> ""
    }

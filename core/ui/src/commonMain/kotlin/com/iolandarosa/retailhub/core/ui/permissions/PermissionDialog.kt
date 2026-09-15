/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.permissions

import org.jetbrains.compose.resources.StringResource

sealed interface PermissionDialogActionType {
    data object OpenSettings : PermissionDialogActionType

    data object CameraRational : PermissionDialogActionType
}

data class PermissionDialog(
    val titleId: StringResource,
    val descriptionId: StringResource,
    val confirmButtonLabelId: StringResource,
    val type: PermissionDialogActionType,
)

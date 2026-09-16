/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.permissions

internal class PermissionControllerImpl(
    val delegate: PermissionControllerDelegate,
) : PermissionController {
    override suspend fun checkPermission(permission: AppPermission): AppPermissionStatus =
        delegate.checkPermission(permission)

    override suspend fun requestPermission(permission: AppPermission): AppPermissionStatus =
        delegate.requestPermission(permission)

    override fun launchSettings() {
        delegate.launchSettings()
    }
}

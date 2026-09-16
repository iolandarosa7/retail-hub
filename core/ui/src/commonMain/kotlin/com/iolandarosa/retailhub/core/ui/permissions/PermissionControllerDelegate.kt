/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.permissions

interface PermissionControllerDelegate {
    suspend fun checkPermission(permission: AppPermission): AppPermissionStatus

    suspend fun requestPermission(permission: AppPermission): AppPermissionStatus

    fun launchSettings()
}

/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.permissions

sealed interface AppPermissionStatus {
    data object Granted : AppPermissionStatus

    data object Denied : AppPermissionStatus

    data class ShouldRequest(
        val showRational: Boolean,
    ) : AppPermissionStatus
}

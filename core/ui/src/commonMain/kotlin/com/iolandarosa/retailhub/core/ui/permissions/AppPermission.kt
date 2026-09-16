/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.permissions

sealed interface AppPermission {
    data object Gallery : AppPermission

    data object Camera : AppPermission
}

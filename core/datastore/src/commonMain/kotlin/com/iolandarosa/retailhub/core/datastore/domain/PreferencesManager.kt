/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.datastore.domain

import kotlinx.coroutines.flow.Flow

interface PreferencesManager {
    fun hasRequestedPermission(permission: String): Flow<Boolean>

    suspend fun setPermissionRequested(permission: String)
}

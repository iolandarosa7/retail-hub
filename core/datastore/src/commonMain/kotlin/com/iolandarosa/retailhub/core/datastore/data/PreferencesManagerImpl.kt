/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.datastore.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.iolandarosa.retailhub.core.datastore.domain.PreferencesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

internal class PreferencesManagerImpl(
    private val dataStore: DataStore<Preferences>,
) : PreferencesManager {
    override fun hasRequestedPermission(permission: String): Flow<Boolean> =
        dataStore.data
            .catch { emptyPreferences() }
            .map {
                it[booleanPreferencesKey(permission)] ?: return@map false
            }

    override suspend fun setPermissionRequested(permission: String) {
        dataStore.edit { it[booleanPreferencesKey(permission)] = true }
    }
}

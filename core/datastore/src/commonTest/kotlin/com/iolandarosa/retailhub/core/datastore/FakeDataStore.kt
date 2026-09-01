/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.FileSystem
import okio.SYSTEM
import kotlin.random.Random

class FakeDataStore {
    private val path =
        FileSystem.SYSTEM_TEMPORARY_DIRECTORY
            .resolve("retailhub-${Random.nextLong()}.preferences_pb")

    val dataStore: DataStore<Preferences> =
        PreferenceDataStoreFactory.createWithPath {
            path
        }

    fun cleanup() {
        FileSystem.SYSTEM.delete(
            path,
            mustExist = false,
        )
    }
}

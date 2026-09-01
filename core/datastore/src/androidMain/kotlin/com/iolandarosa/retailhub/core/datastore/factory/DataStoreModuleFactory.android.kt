/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.datastore.factory

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

actual class DataStoreModuleFactory(
    private val context: Context,
) {
    actual fun create(): DataStore<Preferences> =
        createDataStore {
            context.filesDir.resolve(DATASTORE_FILE_NAME).absolutePath
        }
}

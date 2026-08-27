package com.iolandarosa.retailhub.core.datastore.factory

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
internal expect class DataStoreModuleFactory {
    fun create(): DataStore<Preferences>
}
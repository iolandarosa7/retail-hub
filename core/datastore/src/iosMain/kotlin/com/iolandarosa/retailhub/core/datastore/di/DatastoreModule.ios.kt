package com.iolandarosa.retailhub.core.datastore.di

import com.iolandarosa.retailhub.core.datastore.factory.DataStoreModuleFactory
import org.koin.dsl.module

actual val platformDataStoreModule = module {
    single { DataStoreModuleFactory().create() }
}
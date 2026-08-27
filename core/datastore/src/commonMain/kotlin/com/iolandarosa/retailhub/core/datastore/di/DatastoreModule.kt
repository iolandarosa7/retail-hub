package com.iolandarosa.retailhub.core.datastore.di

import com.iolandarosa.retailhub.core.datastore.data.TokenManagerImpl
import com.iolandarosa.retailhub.core.datastore.domain.TokenManager
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformDataStoreModule: Module

val datastoreModule = module {
    includes(platformDataStoreModule)

    single<TokenManager> { TokenManagerImpl(get()) }
}
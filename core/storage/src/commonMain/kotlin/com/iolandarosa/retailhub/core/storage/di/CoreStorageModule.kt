/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.storage.di

import com.iolandarosa.retailhub.core.storage.data.ImageStorageImpl
import com.iolandarosa.retailhub.core.storage.domain.ImageStorage
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformImageStorageModule: Module

val coreStorageModule =
    module {
        includes(platformImageStorageModule)
        single<ImageStorage> { ImageStorageImpl(delegate = get()) }
    }

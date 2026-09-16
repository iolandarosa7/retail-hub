/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.storage.di

import com.iolandarosa.retailhub.core.storage.data.AndroidLocalImageStorageImpl
import com.iolandarosa.retailhub.core.storage.domain.LocalImageStorageDelegate
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformImageStorageModule: Module =
    module {
        single<LocalImageStorageDelegate> { AndroidLocalImageStorageImpl(get(), get()) }
    }

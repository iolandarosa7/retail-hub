/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.storage.di

import com.iolandarosa.retailhub.core.storage.data.AndroidImageStorageImpl
import com.iolandarosa.retailhub.core.storage.domain.ImageStorageDelegate
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformImageStorageModule: Module =
    module {
        single<ImageStorageDelegate> { AndroidImageStorageImpl(get(), get()) }
    }

/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.common.di

import com.iolandarosa.retailhub.core.common.dispatcher.DefaultDispatcherProvider
import com.iolandarosa.retailhub.core.common.dispatcher.DispatcherProvider
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformClipboardModule: Module

val coreCommonModule =
    module {
        includes(platformClipboardModule)
        single<DispatcherProvider> { DefaultDispatcherProvider }
    }

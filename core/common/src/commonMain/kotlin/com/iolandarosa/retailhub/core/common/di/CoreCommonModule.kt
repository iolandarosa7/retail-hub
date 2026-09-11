/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.common.di

import com.iolandarosa.retailhub.core.common.dispatcher.DefaultDispatcherProvider
import com.iolandarosa.retailhub.core.common.dispatcher.DispatcherProvider
import com.iolandarosa.retailhub.core.common.maps.MapManager
import com.iolandarosa.retailhub.core.common.maps.MapManagerImpl
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformClipboardModule: Module
expect val platformMapsModule: Module

val coreCommonModule =
    module {
        includes(platformClipboardModule, platformMapsModule)
        single<DispatcherProvider> { DefaultDispatcherProvider }
        single<MapManager> { MapManagerImpl(get()) }
    }

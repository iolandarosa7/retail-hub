/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.common.di

import com.iolandarosa.retailhub.core.common.dispatcher.DefaultDispatcherProvider
import com.iolandarosa.retailhub.core.common.dispatcher.DispatcherProvider
import org.koin.dsl.module

val coreCommonModule =
    module {
        single<DispatcherProvider> { DefaultDispatcherProvider }
    }

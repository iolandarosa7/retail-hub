/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.common.di

import com.iolandarosa.retailhub.core.common.clipboard.AndroidAppClipboardManager
import com.iolandarosa.retailhub.core.common.clipboard.AppClipboardManager
import com.iolandarosa.retailhub.core.common.maps.AndroidMapNavigator
import com.iolandarosa.retailhub.core.common.maps.MapNavigator
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformClipboardModule: Module
    get() =
        module {
            single<AppClipboardManager> { AndroidAppClipboardManager(get()) }
        }

actual val platformMapsModule: Module
    get() =
        module {
            single<MapNavigator> { AndroidMapNavigator(get()) }
        }

/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.common.di

import com.iolandarosa.retailhub.core.common.clipboard.AndroidAppClipboardManager
import com.iolandarosa.retailhub.core.common.clipboard.AppClipboardManager
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformClipboardModule: Module
    get() =
        module {
            single<AppClipboardManager> { AndroidAppClipboardManager(get()) }
        }

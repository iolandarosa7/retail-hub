/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.common.di

import com.iolandarosa.retailhub.core.common.clipboard.AppClipboardManager
import com.iolandarosa.retailhub.core.common.clipboard.IosClipboardManager
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformClipboardModule: Module
    get() =
        module {
            single<AppClipboardManager> { IosClipboardManager() }
        }

/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.di

import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformUiModule: Module

val coreUiModule =
    module {
        includes(platformUiModule)
    }

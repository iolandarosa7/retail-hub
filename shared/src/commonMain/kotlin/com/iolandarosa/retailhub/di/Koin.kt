package com.iolandarosa.retailhub.di

import com.iolandarosa.retailhub.features.auth.di.authModule
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(
            networkModule,
            authModule,
        )
    }
}
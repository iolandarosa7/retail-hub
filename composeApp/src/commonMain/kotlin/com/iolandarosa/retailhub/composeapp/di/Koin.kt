package com.iolandarosa.retailhub.composeapp.di

import com.iolandarosa.retailhub.core.network.di.networkModule
import com.iolandarosa.retailhub.features.auth.di.authModule
import org.koin.core.context.startKoin

val appModules = listOf(
    networkModule,
    authModule,
)
fun initKoin() {
    startKoin { modules(appModules) }
}
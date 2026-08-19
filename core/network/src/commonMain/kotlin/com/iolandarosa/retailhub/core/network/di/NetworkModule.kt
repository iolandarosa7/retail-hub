package com.iolandarosa.retailhub.core.network.di

import com.iolandarosa.retailhub.core.network.client.createHttpClient
import org.koin.dsl.module

val networkModule = module {
    single { createHttpClient() }
}
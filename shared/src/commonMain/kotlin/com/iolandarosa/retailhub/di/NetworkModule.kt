package com.iolandarosa.retailhub.di

import com.iolandarosa.retailhub.core.network.createHttpClient
import org.koin.dsl.module

val networkModule = module {
    single { createHttpClient() }
}
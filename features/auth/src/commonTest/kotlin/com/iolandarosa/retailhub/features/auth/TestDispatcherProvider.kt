package com.iolandarosa.retailhub.features.auth

import com.iolandarosa.retailhub.core.common.dispatcher.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher

class TestDispatcherProvider(private val dispatcher: CoroutineDispatcher): DispatcherProvider {
    override val main: CoroutineDispatcher
        get() = dispatcher
    override val io: CoroutineDispatcher
        get() = dispatcher
    override val default: CoroutineDispatcher
        get() = dispatcher
}
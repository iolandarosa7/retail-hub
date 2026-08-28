package com.iolandarosa.retailhub.composeapp

import com.iolandarosa.retailhub.core.datastore.domain.TokenManager
import dev.mokkery.mock
import org.koin.dsl.module

val fakeTestModule = module {
    // overrides TokenManager to avoid creating with the real datastore instance
    single<TokenManager> { mock() }
}
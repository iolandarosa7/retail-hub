package com.iolandarosa.retailhub.core.network.di

import com.iolandarosa.retailhub.core.model.NetworkClientType
import com.iolandarosa.retailhub.core.network.client.createAuthenticatedClient
import com.iolandarosa.retailhub.core.network.client.createPublicClient
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule = module {
    single(named(NetworkClientType.PUBLIC)) { createPublicClient() }

    single(named(NetworkClientType.AUTHENTICATED)) {
        createAuthenticatedClient(
            tokenManager = get(),
            publicClient = get(named(NetworkClientType.PUBLIC))
        )
    }
}
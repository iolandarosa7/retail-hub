/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.composeapp.di

import com.iolandarosa.retailhub.core.common.di.coreCommonModule
import com.iolandarosa.retailhub.core.datastore.di.datastoreModule
import com.iolandarosa.retailhub.core.network.di.networkModule
import com.iolandarosa.retailhub.core.ui.di.coreUiModule
import com.iolandarosa.retailhub.features.auth.di.authModule
import com.iolandarosa.retailhub.features.profile.di.profileModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

val appModules =
    listOf(
        coreCommonModule,
        datastoreModule,
        networkModule,
        coreUiModule,
        authModule,
        profileModule,
    )

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(appModules)
    }
}

/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.composeapp.di

import com.iolandarosa.retailhub.composeapp.fakeTestModule
import com.iolandarosa.retailhub.core.common.dispatcher.DispatcherProvider
import com.iolandarosa.retailhub.core.datastore.domain.TokenManager
import com.iolandarosa.retailhub.core.storage.domain.LocalImageStorage
import com.iolandarosa.retailhub.features.profile.data.remote.ProfileRemoteDataSource
import com.iolandarosa.retailhub.features.profile.domain.interactors.LoginUseCase
import com.iolandarosa.retailhub.features.profile.domain.repository.ProfileRepository
import com.iolandarosa.retailhub.features.profile.presentation.profile.ProfileViewModel
import org.koin.dsl.koinApplication
import kotlin.test.Test
import kotlin.test.assertNotNull

class KoinTest {
    @Test
    fun applicationDependencyGraphIsValid() {
        val koinApp =
            koinApplication {
                allowOverride(true) // this line allows fakeTestModules to override appModules
                modules(appModules)
                modules(fakeTestModule)
            }

        assertNotNull(koinApp.koin.get<DispatcherProvider>())
        assertNotNull(koinApp.koin.get<LocalImageStorage>())
        assertNotNull(koinApp.koin.get<TokenManager>())
        assertNotNull(koinApp.koin.get<ProfileRemoteDataSource>())
        assertNotNull(koinApp.koin.get<ProfileRepository>())
        assertNotNull(koinApp.koin.get<LoginUseCase>())
        assertNotNull(koinApp.koin.get<ProfileViewModel>())

        koinApp.close()
    }
}

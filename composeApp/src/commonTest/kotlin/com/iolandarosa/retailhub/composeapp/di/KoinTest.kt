package com.iolandarosa.retailhub.composeapp.di

import com.iolandarosa.retailhub.composeapp.fakeTestModule
import com.iolandarosa.retailhub.core.common.dispatcher.DispatcherProvider
import com.iolandarosa.retailhub.features.auth.data.remote.AuthRemoteDataSource
import com.iolandarosa.retailhub.features.auth.domain.repository.AuthenticationRepository
import com.iolandarosa.retailhub.features.auth.domain.usecase.LoginUseCase
import com.iolandarosa.retailhub.features.auth.login.LoginViewModel
import org.koin.dsl.koinApplication
import kotlin.test.Test
import kotlin.test.assertNotNull

class KoinTest {
    @Test
    fun `application dependency graph is valid`() {
        val koinApp = koinApplication {
            allowOverride(true) // this line allows fakeTestModules to override appModules
            modules(appModules)
            modules(fakeTestModule)
        }

        assertNotNull(koinApp.koin.get<DispatcherProvider>())
        assertNotNull(koinApp.koin.get<AuthRemoteDataSource>())
        assertNotNull(koinApp.koin.get<AuthenticationRepository>())
        assertNotNull(koinApp.koin.get<LoginUseCase>())
        assertNotNull(koinApp.koin.get<LoginViewModel>())

        koinApp.close()
    }
}
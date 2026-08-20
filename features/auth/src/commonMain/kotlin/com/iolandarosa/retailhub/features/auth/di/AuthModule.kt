package com.iolandarosa.retailhub.features.auth.di

import com.iolandarosa.retailhub.features.auth.data.remote.AuthRemoteDataSource
import com.iolandarosa.retailhub.features.auth.data.remote.AuthRemoteDataSourceImpl
import com.iolandarosa.retailhub.features.auth.data.repository.AuthenticationRepositoryImpl
import com.iolandarosa.retailhub.features.auth.domain.repository.AuthenticationRepository
import com.iolandarosa.retailhub.features.auth.domain.usecase.LoginUseCase
import com.iolandarosa.retailhub.features.auth.login.LoginViewModel
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel

val authModule = module {
    single<AuthRemoteDataSource> { AuthRemoteDataSourceImpl(get()) }
    single<AuthenticationRepository> {
        AuthenticationRepositoryImpl(
            get()
        )
    }
    factory { LoginUseCase(get()) }
    viewModel { LoginViewModel(get()) }
}
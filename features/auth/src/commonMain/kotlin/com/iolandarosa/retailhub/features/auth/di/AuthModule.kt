/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.di

import com.iolandarosa.retailhub.core.model.NetworkClientType
import com.iolandarosa.retailhub.features.auth.data.remote.AuthRemoteDataSource
import com.iolandarosa.retailhub.features.auth.data.remote.AuthRemoteDataSourceImpl
import com.iolandarosa.retailhub.features.auth.data.repository.AuthenticationRepositoryImpl
import com.iolandarosa.retailhub.features.auth.domain.interactors.GetAuthUserUseCase
import com.iolandarosa.retailhub.features.auth.domain.interactors.GetAuthUserUseCaseImpl
import com.iolandarosa.retailhub.features.auth.domain.interactors.LoginUseCase
import com.iolandarosa.retailhub.features.auth.domain.interactors.LoginUseCaseImpl
import com.iolandarosa.retailhub.features.auth.domain.repository.AuthenticationRepository
import com.iolandarosa.retailhub.features.auth.presentation.login.LoginViewModel
import com.iolandarosa.retailhub.features.auth.presentation.profile.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val authModule =
    module {
        single<AuthRemoteDataSource> {
            AuthRemoteDataSourceImpl(
                publicClient = get(named(NetworkClientType.PUBLIC)),
                authenticatedClient = get(named(NetworkClientType.AUTHENTICATED)),
            )
        }
        single<AuthenticationRepository> {
            AuthenticationRepositoryImpl(
                service = get(),
                tokenManager = get(),
            )
        }
        factory<LoginUseCase> { LoginUseCaseImpl(get()) }
        factory<GetAuthUserUseCase> { GetAuthUserUseCaseImpl(get()) }
        viewModel { LoginViewModel(loginUseCase = get(), dispatcherProvider = get()) }
        viewModel { ProfileViewModel(getAuthUserUseCase = get(), dispatcherProvider = get()) }
    }

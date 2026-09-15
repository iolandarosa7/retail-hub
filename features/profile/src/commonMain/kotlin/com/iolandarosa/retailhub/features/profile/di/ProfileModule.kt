/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.di

import com.iolandarosa.retailhub.core.model.NetworkClientType
import com.iolandarosa.retailhub.features.profile.data.remote.ProfileRemoteDataSource
import com.iolandarosa.retailhub.features.profile.data.remote.ProfileRemoteDataSourceImpl
import com.iolandarosa.retailhub.features.profile.data.repository.ProfileRepositoryImpl
import com.iolandarosa.retailhub.features.profile.domain.interactors.GetAuthUserUseCase
import com.iolandarosa.retailhub.features.profile.domain.interactors.GetAuthUserUseCaseImpl
import com.iolandarosa.retailhub.features.profile.domain.interactors.LogoutUseCase
import com.iolandarosa.retailhub.features.profile.domain.interactors.LogoutUseCaseImpl
import com.iolandarosa.retailhub.features.profile.domain.repository.ProfileRepository
import com.iolandarosa.retailhub.features.profile.presentation.address.AddressViewModel
import com.iolandarosa.retailhub.features.profile.presentation.profile.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val profileModule =
    module {
        single<ProfileRemoteDataSource> {
            ProfileRemoteDataSourceImpl(
                authenticatedClient = get(named(NetworkClientType.AUTHENTICATED)),
            )
        }
        single<ProfileRepository> {
            ProfileRepositoryImpl(service = get(), tokenManager = get())
        }
        factory<GetAuthUserUseCase> { GetAuthUserUseCaseImpl(get()) }
        factory<LogoutUseCase> { LogoutUseCaseImpl(get()) }
        viewModel {
            ProfileViewModel(
                getAuthUserUseCase = get(),
                logoutUseCase = get(),
                dispatcherProvider = get(),
                permissionController = get(),
                imagePickerController = get(),
                preferencesManager = get(),
            )
        }
        viewModel { params ->
            AddressViewModel(
                address = params.get(),
                dispatcherProvider = get(),
                clipboardManager = get(),
                mapManager = get(),
            )
        }
    }

/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.user.di

import com.iolandarosa.retailhub.core.model.NetworkClientType
import com.iolandarosa.retailhub.core.user.data.remote.UserRemoteDataSource
import com.iolandarosa.retailhub.core.user.data.remote.UserRemoteDataSourceImpl
import com.iolandarosa.retailhub.core.user.data.repository.UserRepositoryImpl
import com.iolandarosa.retailhub.core.user.domain.interactors.GetAuthUserUseCase
import com.iolandarosa.retailhub.core.user.domain.interactors.GetAuthUserUseCaseImpl
import com.iolandarosa.retailhub.core.user.domain.interactors.GetLocalUserImageUseCase
import com.iolandarosa.retailhub.core.user.domain.interactors.GetLocalUserImageUseCaseImpl
import com.iolandarosa.retailhub.core.user.domain.repository.UserRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val coreUserModule =
    module {
        single<UserRemoteDataSource> {
            UserRemoteDataSourceImpl(
                authenticatedClient = get(named(NetworkClientType.AUTHENTICATED)),
            )
        }
        single<UserRepository> {
            UserRepositoryImpl(service = get(), localImageStorage = get())
        }
        factory<GetAuthUserUseCase> { GetAuthUserUseCaseImpl(get()) }
        factory<GetLocalUserImageUseCase> { GetLocalUserImageUseCaseImpl(get()) }
    }
